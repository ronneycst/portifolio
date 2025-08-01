/**
 * Projetos Controller
 * Responsável por todas as funcionalidades relacionadas a projetos
 */

class ProjetosController {
  constructor() {
    this.projetos = [];
    this.currentProjeto = null;
    this.isEditing = false;
  }

  async init() {
    try {
      LoadingService.show();
      await this.loadProjetos();
      await this.loadGerentes();
      this.updateUI();
    } catch (error) {
      console.error('Erro ao carregar projetos:', error);
      NotificationService.showError('Erro ao carregar projetos');
    } finally {
      LoadingService.hide();
    }
  }

  async loadProjetos() {
    try {
      this.projetos = await api.getProjetos();
    } catch (error) {
      console.error('Erro ao carregar projetos:', error);
      throw error;
    }
  }

  async loadGerentes() {
    try {
      const pessoas = await api.getPessoas();
      const gerentes = pessoas.filter(p => p.gerente);

      const select = document.getElementById('projetoGerente');
      if (select) {
        select.innerHTML = '<option value="">Selecione um gerente</option>';
        gerentes.forEach(gerente => {
          select.innerHTML += `<option value="${gerente.id}">${gerente.nome}</option>`;
        });
      }
    } catch (error) {
      console.error('Erro ao carregar gerentes:', error);
      throw error;
    }
  }

  updateUI() {
    this.updateTable();
  }


  updateTable() {
    const tbody = document.getElementById('projetosTableBody');
    if (!tbody) return;

    if (this.projetos.length === 0) {
      tbody.innerHTML = `
                <tr>
                    <td colspan="8" class="text-center">
                        <div class="empty-state">
                            <i class="bi bi-folder"></i>
                            <h5>Nenhum projeto encontrado</h5>
                            <p>Não há projetos cadastrados no sistema.</p>
                            <button class="btn btn-primary" onclick="showProjetoModal()">
                                <i class="bi bi-plus"></i> Criar Primeiro Projeto
                            </button>
                        </div>
                    </td>
                </tr>
            `;
      return;
    }

    const rowsHTML = this.projetos.map(projeto => `
            <tr>
                <td>${projeto.id}</td>
                <td>
                    <strong>${projeto.nome}</strong>
                    ${projeto.descricao ? `<br><small class="text-muted">${projeto.descricao}</small>` : ''}
                </td>
                <td>${projeto.gerenteResponsavel ? projeto.gerenteResponsavel.nome : '-'}</td>
                <td>${DataFormatter.formatDate(projeto.dataInicio)}</td>
                <td>
                    <span class="badge badge-status ${DataFormatter.getStatusClass(projeto.status)}">
                        ${DataFormatter.formatProjectStatus(projeto.status)}
                    </span>
                </td>
                <td>
                    <span class="badge badge-risk ${DataFormatter.getRiskClass(projeto.risco)}">
                        ${DataFormatter.formatProjectRisk(projeto.risco)}
                    </span>
                </td>
                <td>${DataFormatter.formatCurrency(projeto.orcamentoTotal)}</td>
                <td>
                    <div class="btn-group" role="group">
                        <button class="btn btn-sm btn-outline-primary" 
                                onclick="viewProjeto(${projeto.id})" 
                                title="Visualizar">
                            <i class="bi bi-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-secondary" 
                                onclick="editProjeto(${projeto.id})" 
                                title="Editar">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" 
                                onclick="deleteProjeto(${projeto.id})" 
                                title="Excluir">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');

    tbody.innerHTML = rowsHTML;
  }


  showModal(projeto = null) {
    this.currentProjeto = projeto;
    this.isEditing = !!projeto;

    const modal = document.getElementById('projetoModal');
    const title = document.getElementById('projetoModalTitle');
    const form = document.getElementById('projetoForm');

    if (title) {
      title.textContent = this.isEditing ? 'Editar Projeto' : 'Novo Projeto';
    }

    if (form) {
      form.reset();
    }

    if (projeto) {
      this.fillForm(projeto);
    }

    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
  }

  fillForm(projeto) {
    const fields = {
      'projetoNome': projeto.nome,
      'projetoGerente': projeto.gerenteResponsavel ? projeto.gerenteResponsavel.id : '',
      'projetoDataInicio': projeto.dataInicio,
      'projetoPrevisaoTermino': projeto.previsaoTermino || '',
      'projetoStatus': projeto.status,
      'projetoRisco': projeto.risco,
      'projetoOrcamento': projeto.orcamentoTotal || '',
      'projetoDescricao': projeto.descricao || ''
    };

    Object.keys(fields).forEach(fieldId => {
      const element = document.getElementById(fieldId);
      if (element) {
        element.value = fields[fieldId];
      }
    });
  }

  async saveProjeto() {
    try {
      const formData = this.getFormData();

      if (!this.validateForm(formData)) {
        return;
      }

      LoadingService.show();

      if (this.isEditing) {
        formData.id = this.currentProjeto.id;
        await api.updateProjeto(formData);
        NotificationService.showSuccess('Projeto atualizado com sucesso!');
      } else {
        await api.createProjeto(formData);
        NotificationService.showSuccess('Projeto criado com sucesso!');
      }

      const modal = bootstrap.Modal.getInstance(document.getElementById('projetoModal'));
      modal.hide();

      await this.init();

      if (document.getElementById('dashboardContent').style.display !== 'none') {
        await dashboard.refresh();
      }

    } catch (error) {
      console.error('Erro ao salvar projeto:', error);
      NotificationService.showError('Erro ao salvar projeto');
    } finally {
      LoadingService.hide();
    }
  }

  getFormData() {
    return {
      nome: document.getElementById('projetoNome').value,
      gerenteResponsavelId: document.getElementById('projetoGerente').value,
      dataInicio: document.getElementById('projetoDataInicio').value,
      previsaoTermino: document.getElementById('projetoPrevisaoTermino').value || null,
      status: document.getElementById('projetoStatus').value,
      risco: document.getElementById('projetoRisco').value,
      orcamentoTotal: document.getElementById('projetoOrcamento').value || null,
      descricao: document.getElementById('projetoDescricao').value || null
    };
  }

  validateForm(data) {
    const requiredFields = ['nome', 'gerenteResponsavelId', 'dataInicio', 'status', 'risco'];

    for (const field of requiredFields) {
      if (!data[field]) {
        NotificationService.showError(`Campo obrigatório não preenchido: ${field}`);
        return false;
      }
    }

    return true;
  }

  async viewProjeto(id) {
    try {
      const projeto = await api.getProjeto(id);
      this.showProjetoDetails(projeto);
    } catch (error) {
      console.error('Erro ao carregar projeto:', error);
      NotificationService.showError('Erro ao carregar projeto');
    }
  }


  showProjetoDetails(projeto) {
    const detailsHTML = `
            <div class="modal fade" id="projetoDetailsModal" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Detalhes do Projeto</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <h6>Informações Básicas</h6>
                                    <p><strong>Nome:</strong> ${projeto.nome}</p>
                                    <p><strong>Gerente:</strong> ${projeto.gerenteResponsavel ? projeto.gerenteResponsavel.nome : '-'}</p>
                                    <p><strong>Data de Início:</strong> ${DataFormatter.formatDate(projeto.dataInicio)}</p>
                                    <p><strong>Previsão de Término:</strong> ${DataFormatter.formatDate(projeto.previsaoTermino)}</p>
                                </div>
                                <div class="col-md-6">
                                    <h6>Status e Risco</h6>
                                    <p><strong>Status:</strong> 
                                        <span class="badge badge-status ${DataFormatter.getStatusClass(projeto.status)}">
                                            ${DataFormatter.formatProjectStatus(projeto.status)}
                                        </span>
                                    </p>
                                    <p><strong>Risco:</strong> 
                                        <span class="badge badge-risk ${DataFormatter.getRiskClass(projeto.risco)}">
                                            ${DataFormatter.formatProjectRisk(projeto.risco)}
                                        </span>
                                    </p>
                                    <p><strong>Orçamento:</strong> ${DataFormatter.formatCurrency(projeto.orcamentoTotal)}</p>
                                </div>
                            </div>
                            ${projeto.descricao ? `
                                <div class="row mt-3">
                                    <div class="col-12">
                                        <h6>Descrição</h6>
                                        <p>${projeto.descricao}</p>
                                    </div>
                                </div>
                            ` : ''}
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                            <button type="button" class="btn btn-primary" onclick="editProjeto(${projeto.id})">Editar</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

    const existingModal = document.getElementById('projetoDetailsModal');
    if (existingModal) {
      existingModal.remove();
    }

    document.body.insertAdjacentHTML('beforeend', detailsHTML);

    const modal = new bootstrap.Modal(document.getElementById('projetoDetailsModal'));
    modal.show();
  }

  async deleteProjeto(id) {
    if (!confirm('Tem certeza que deseja excluir este projeto?')) {
      return;
    }

    try {
      LoadingService.show();
      await api.deleteProjeto({ id });
      NotificationService.showSuccess('Projeto excluído com sucesso!');
      await this.init();

      if (document.getElementById('dashboardContent').style.display !== 'none') {
        await dashboard.refresh();
      }
    } catch (error) {
      console.error('Erro ao excluir projeto:', error);
      NotificationService.showError('Erro ao excluir projeto');
    } finally {
      LoadingService.hide();
    }
  }
}

const projetosController = new ProjetosController();

function showProjetos() {
  hideAllContents();

  const projetosContent = document.getElementById('projetosContent');
  if (projetosContent) {
    projetosContent.style.display = 'block';
    projetosContent.classList.add('fade-in');
  }

  updateNavigation('projetos');
  projetosController.init();
}


function showProjetoModal() {
  projetosController.showModal();
}

function saveProjeto() {
  projetosController.saveProjeto();
}

function viewProjeto(id) {
  projetosController.viewProjeto(id);
}

function editProjeto(id) {
  const projeto = projetosController.projetos.find(p => p.id === id);
  if (projeto) {
    projetosController.showModal(projeto);
  }
}

function deleteProjeto(id) {
  projetosController.deleteProjeto(id);
} 