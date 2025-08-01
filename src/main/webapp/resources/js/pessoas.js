class PessoasController {
  constructor() {
    this.pessoas = [];
    this.currentPessoa = null;
    this.isEditing = false;
  }

  async init() {
    try {
      LoadingService.show();
      await this.loadPessoas();
      this.updateUI();
    } catch (error) {
      console.error('Erro ao carregar pessoas:', error);
      NotificationService.showError('Erro ao carregar pessoas');
    } finally {
      LoadingService.hide();
    }
  }

  async loadPessoas() {
    try {
      this.pessoas = await api.getPessoas();
    } catch (error) {
      console.error('Erro ao carregar pessoas:', error);
      throw error;
    }
  }

  updateUI() {
    this.updateTable();
  }
updateTable() {
    const tbody = document.getElementById('pessoasTableBody');
    if (!tbody) return;

    if (this.pessoas.length === 0) {
      tbody.innerHTML = `
                <tr>
                    <td colspan="7" class="text-center">
                        <div class="empty-state">
                            <i class="bi bi-people"></i>
                            <h5>Nenhuma pessoa encontrada</h5>
                            <p>Não há pessoas cadastradas no sistema.</p>
                            <button class="btn btn-primary" onclick="showPessoaModal()">
                                <i class="bi bi-plus"></i> Criar Primeira Pessoa
                            </button>
                        </div>
                    </td>
                </tr>
            `;
      return;
    }

    const rowsHTML = this.pessoas.map(pessoa => `
            <tr>
                <td>${pessoa.id}</td>
                <td>
                    <strong>${pessoa.nome}</strong>
                </td>
                <td>${pessoa.cpf || '-'}</td>
                <td>${DataFormatter.formatDate(pessoa.dataNascimento)}</td>
                <td>
                    <span class="badge ${pessoa.funcionario ? 'bg-success' : 'bg-secondary'}">
                        ${pessoa.funcionario ? 'Sim' : 'Não'}
                    </span>
                </td>
                <td>
                    <span class="badge ${pessoa.gerente ? 'bg-primary' : 'bg-secondary'}">
                        ${pessoa.gerente ? 'Sim' : 'Não'}
                    </span>
                </td>
                <td>
                    <div class="btn-group" role="group">
                        <button class="btn btn-sm btn-outline-primary" 
                                onclick="viewPessoa(${pessoa.id})" 
                                title="Visualizar">
                            <i class="bi bi-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-secondary" 
                                onclick="editPessoa(${pessoa.id})" 
                                title="Editar">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" 
                                onclick="deletePessoa(${pessoa.id})" 
                                title="Excluir">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');

    tbody.innerHTML = rowsHTML;
  }

  showModal(pessoa = null) {
    this.currentPessoa = pessoa;
    this.isEditing = !!pessoa;

    const modal = document.getElementById('pessoaModal');
    const title = document.getElementById('pessoaModalTitle');
    const form = document.getElementById('pessoaForm');

    if (title) {
      title.textContent = this.isEditing ? 'Editar Pessoa' : 'Nova Pessoa';
    }

    if (form) {
      form.reset();
    }

    if (pessoa) {
      this.fillForm(pessoa);
    }

    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
  }

  fillForm(pessoa) {
    const fields = {
      'pessoaNome': pessoa.nome,
      'pessoaCpf': pessoa.cpf || '',
      'pessoaDataNascimento': pessoa.dataNascimento || '',
      'pessoaFuncionario': pessoa.funcionario,
      'pessoaGerente': pessoa.gerente
    };

    Object.keys(fields).forEach(fieldId => {
      const element = document.getElementById(fieldId);
      if (element) {
        if (element.type === 'checkbox') {
          element.checked = fields[fieldId];
        } else {
          element.value = fields[fieldId];
        }
      }
    });
  }

  async savePessoa() {
    try {
      const formData = this.getFormData();

      if (!this.validateForm(formData)) {
        return;
      }

      LoadingService.show();

      if (this.isEditing) {
        formData.id = this.currentPessoa.id;
        await api.updatePessoa(formData);
        NotificationService.showSuccess('Pessoa atualizada com sucesso!');
      } else {
        await api.createPessoa(formData);
        NotificationService.showSuccess('Pessoa criada com sucesso!');
      }

      const modal = bootstrap.Modal.getInstance(document.getElementById('pessoaModal'));
      modal.hide();

      await this.init();

      if (document.getElementById('dashboardContent').style.display !== 'none') {
        await dashboard.refresh();
      }

    } catch (error) {
      console.error('Erro ao salvar pessoa:', error);
      NotificationService.showError('Erro ao salvar pessoa');
    } finally {
      LoadingService.hide();
    }
  }

  getFormData() {
    return {
      nome: document.getElementById('pessoaNome').value,
      cpf: document.getElementById('pessoaCpf').value || null,
      dataNascimento: document.getElementById('pessoaDataNascimento').value || null,
      funcionario: document.getElementById('pessoaFuncionario').checked,
      gerente: document.getElementById('pessoaGerente').checked
    };
  }

  validateForm(data) {
    if (!data.nome) {
      NotificationService.showError('Nome é obrigatório');
      return false;
    }

    return true;
  }

  async viewPessoa(id) {
    try {
      const pessoa = await api.getPessoa(id);
      this.showPessoaDetails(pessoa);
    } catch (error) {
      console.error('Erro ao carregar pessoa:', error);
      NotificationService.showError('Erro ao carregar pessoa');
    }
  }

  showPessoaDetails(pessoa) {
    const detailsHTML = `
            <div class="modal fade" id="pessoaDetailsModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Detalhes da Pessoa</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-12">
                                    <h6>Informações Pessoais</h6>
                                    <p><strong>Nome:</strong> ${pessoa.nome}</p>
                                    <p><strong>CPF:</strong> ${pessoa.cpf || '-'}</p>
                                    <p><strong>Data de Nascimento:</strong> ${DataFormatter.formatDate(pessoa.dataNascimento)}</p>
                                </div>
                            </div>
                            <div class="row mt-3">
                                <div class="col-12">
                                    <h6>Informações Profissionais</h6>
                                    <p><strong>Funcionário:</strong> 
                                        <span class="badge ${pessoa.funcionario ? 'bg-success' : 'bg-secondary'}">
                                            ${pessoa.funcionario ? 'Sim' : 'Não'}
                                        </span>
                                    </p>
                                    <p><strong>Gerente:</strong> 
                                        <span class="badge ${pessoa.gerente ? 'bg-primary' : 'bg-secondary'}">
                                            ${pessoa.gerente ? 'Sim' : 'Não'}
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                            <button type="button" class="btn btn-primary" onclick="editPessoa(${pessoa.id})">Editar</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

    const existingModal = document.getElementById('pessoaDetailsModal');
    if (existingModal) {
      existingModal.remove();
    }

    document.body.insertAdjacentHTML('beforeend', detailsHTML);

    const modal = new bootstrap.Modal(document.getElementById('pessoaDetailsModal'));
    modal.show();
  }

  async deletePessoa(id) {
    if (!confirm('Tem certeza que deseja excluir esta pessoa?')) {
      return;
    }

    try {
      LoadingService.show();
      await api.deletePessoa({ id });
      NotificationService.showSuccess('Pessoa excluída com sucesso!');
      await this.init();

      if (document.getElementById('dashboardContent').style.display !== 'none') {
        await dashboard.refresh();
      }
    } catch (error) {
      console.error('Erro ao excluir pessoa:', error);
      NotificationService.showError('Erro ao excluir pessoa');
    } finally {
      LoadingService.hide();
    }
  }
}

const pessoasController = new PessoasController();


function showPessoas() {
  hideAllContents();

  const pessoasContent = document.getElementById('pessoasContent');
  if (pessoasContent) {
    pessoasContent.style.display = 'block';
    pessoasContent.classList.add('fade-in');
  }

  updateNavigation('pessoas');
  pessoasController.init();
}


function showPessoaModal() {
  pessoasController.showModal();
}

function savePessoa() {
  pessoasController.savePessoa();
}

function viewPessoa(id) {
  pessoasController.viewPessoa(id);
}

function editPessoa(id) {
  const pessoa = pessoasController.pessoas.find(p => p.id === id);
  if (pessoa) {
    pessoasController.showModal(pessoa);
  }
}

function deletePessoa(id) {
  pessoasController.deletePessoa(id);
} 