/**
 * Membros Controller
 * Responsável por todas as funcionalidades relacionadas a membros
 */

class MembrosController {
  constructor() {
    this.membros = [];
    this.currentMembro = null;
    this.isEditing = false;
  }

  /**
   * Inicializar módulo de membros
   */
  async init() {
    try {
      LoadingService.show();
      await this.loadMembros();
      await this.loadPessoas();
      await this.loadProjetos();
      this.updateUI();
    } catch (error) {
      console.error('Erro ao carregar membros:', error);
      NotificationService.showError('Erro ao carregar membros');
    } finally {
      LoadingService.hide();
    }
  }

  /**
   * Carregar todos os membros
   */
  async loadMembros() {
    try {
      this.membros = await api.getMembros();
    } catch (error) {
      console.error('Erro ao carregar membros:', error);
      throw error;
    }
  }

  /**
   * Carregar pessoas para o select
   */
  async loadPessoas() {
    try {
      const pessoas = await api.getPessoas();

      const select = document.getElementById('membroPessoa');
      if (select) {
        select.innerHTML = '<option value="">Selecione uma pessoa</option>';
        pessoas.forEach(pessoa => {
          select.innerHTML += `<option value="${pessoa.id}">${pessoa.nome}</option>`;
        });
      }
    } catch (error) {
      console.error('Erro ao carregar pessoas:', error);
      throw error;
    }
  }

  /**
   * Carregar projetos para o select
   */
  async loadProjetos() {
    try {
      const projetos = await api.getProjetos();

      const select = document.getElementById('membroProjeto');
      if (select) {
        select.innerHTML = '<option value="">Selecione um projeto</option>';
        projetos.forEach(projeto => {
          select.innerHTML += `<option value="${projeto.id}">${projeto.nome}</option>`;
        });
      }
    } catch (error) {
      console.error('Erro ao carregar projetos:', error);
      throw error;
    }
  }


  updateUI() {
    this.updateTable();
  }
 updateTable() {
    const tbody = document.getElementById('membrosTableBody');
    if (!tbody) return;

    if (this.membros.length === 0) {
      tbody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center">
                        <div class="empty-state">
                            <i class="bi bi-person-badge"></i>
                            <h5>Nenhum membro encontrado</h5>
                            <p>Não há membros cadastrados no sistema.</p>
                        </div>
                    </td>
                </tr>
            `;
      return;
    }

    const rowsHTML = this.membros.map(membro => `
            <tr>
                <td>${membro.id}</td>
                <td>
                    <strong>${membro.pessoa ? membro.pessoa.nome : '-'}</strong>
                </td>
                <td>
                    <strong>${membro.projeto ? membro.projeto.nome : '-'}</strong>
                </td>
                <td>${DataFormatter.formatDate(membro.dataInicio)}</td>
                <td>${DataFormatter.formatDate(membro.dataFim)}</td>
            </tr>
        `).join('');

    tbody.innerHTML = rowsHTML;
  }

  showModal(membro = null) {
    this.currentMembro = membro;
    this.isEditing = !!membro;

    const modal = document.getElementById('membroModal');
    const title = document.getElementById('membroModalTitle');
    const form = document.getElementById('membroForm');

    if (form) {
      form.reset();
    }

    if (membro) {
      this.fillForm(membro);
    }

    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
  }

  fillForm(membro) {
    const fields = {
      'membroPessoa': membro.pessoa ? membro.pessoa.id : '',
      'membroProjeto': membro.projeto ? membro.projeto.id : '',
      'membroDataInicio': membro.dataInicio || '',
      'membroDataFim': membro.dataFim || ''
    };

    Object.keys(fields).forEach(fieldId => {
      const element = document.getElementById(fieldId);
      if (element) {
        element.value = fields[fieldId];
      }
    });
  }

  async viewMembro(id) {
    try {
      const membro = await api.getMembro(id);
      this.showMembroDetails(membro);
    } catch (error) {
      console.error('Erro ao carregar membro:', error);
      NotificationService.showError('Erro ao carregar membro');
    }
  }

  showMembroDetails(membro) {
    const detailsHTML = `
            <div class="modal fade" id="membroDetailsModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Detalhes do Membro</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-12">
                                    <h6>Informações do Membro</h6>
                                    <p><strong>Pessoa:</strong> ${membro.pessoa ? membro.pessoa.nome : '-'}</p>
                                    <p><strong>Projeto:</strong> ${membro.projeto ? membro.projeto.nome : '-'}</p>
                                    <p><strong>Data de Início:</strong> ${DataFormatter.formatDate(membro.dataInicio)}</p>
                                    <p><strong>Data de Fim:</strong> ${DataFormatter.formatDate(membro.dataFim)}</p>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                            <button type="button" class="btn btn-primary" onclick="editMembro(${membro.id})">Editar</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

    const existingModal = document.getElementById('membroDetailsModal');
    if (existingModal) {
      existingModal.remove();
    }

    document.body.insertAdjacentHTML('beforeend', detailsHTML);

    const modal = new bootstrap.Modal(document.getElementById('membroDetailsModal'));
    modal.show();
  }
}

const membrosController = new MembrosController();

function showMembros() {
  hideAllContents();

  const membrosContent = document.getElementById('membrosContent');
  if (membrosContent) {
    membrosContent.style.display = 'block';
    membrosContent.classList.add('fade-in');
  }

  updateNavigation('membros');
  membrosController.init();
}

function showMembroModal() {
  membrosController.showModal();
}

function viewMembro(id) {
  membrosController.viewMembro(id);
}