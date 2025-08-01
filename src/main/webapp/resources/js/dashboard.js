class DashboardController {
  constructor() {
    this.stats = {
      totalProjetos: 0,
      projetosAtivos: 0,
      totalPessoas: 0,
      totalMembros: 0
    };
    this.recentProjects = [];
  }

  async init() {
    try {
      LoadingService.show();
      await this.loadStatistics();
      await this.loadRecentProjects();
      this.updateUI();
    } catch (error) {
      console.error('Erro ao carregar dashboard:', error);
      NotificationService.showError('Erro ao carregar dashboard');
    } finally {
      LoadingService.hide();
    }
  }

  async loadStatistics() {
    try {
      const [projetos, pessoas, membros] = await Promise.all([
        api.getProjetos(),
        api.getPessoas(),
        api.getMembros()
      ]);

      this.stats.totalProjetos = projetos.length || 0;
      this.stats.totalPessoas = pessoas.length || 0;
      this.stats.totalMembros = membros.length || 0;

      this.stats.projetosAtivos = projetos.filter(p =>
        p.status !== 'ENCERRADO' && p.status !== 'CANCELADO'
      ).length || 0;

    } catch (error) {
      console.error('Erro ao carregar estatísticas:', error);
      throw error;
    }
  }

  async loadRecentProjects() {
    try {
      const projetos = await api.getProjetos();

      this.recentProjects = projetos
        .sort((a, b) => new Date(b.dataInicio) - new Date(a.dataInicio))
        .slice(0, 5); // Pegar apenas os 5 mais recentes

    } catch (error) {
      console.error('Erro ao carregar projetos recentes:', error);
      throw error;
    }
  }

  updateUI() {
    this.updateStatistics();
    this.updateRecentProjects();
  }

  updateStatistics() {
    const elements = {
      totalProjetos: document.getElementById('totalProjetos'),
      projetosAtivos: document.getElementById('projetosAtivos'),
      totalPessoas: document.getElementById('totalPessoas'),
      totalMembros: document.getElementById('totalMembros')
    };

    if (elements.totalProjetos) {
      elements.totalProjetos.textContent = this.stats.totalProjetos;
    }
    if (elements.projetosAtivos) {
      elements.projetosAtivos.textContent = this.stats.projetosAtivos;
    }
    if (elements.totalPessoas) {
      elements.totalPessoas.textContent = this.stats.totalPessoas;
    }
    if (elements.totalMembros) {
      elements.totalMembros.textContent = this.stats.totalMembros;
    }
  }

  updateRecentProjects() {
    const container = document.getElementById('recentProjectsList');
    if (!container) return;

    if (this.recentProjects.length === 0) {
      container.innerHTML = `
                <div class="empty-state">
                    <i class="bi bi-folder"></i>
                    <h5>Nenhum projeto encontrado</h5>
                    <p>Não há projetos cadastrados no sistema.</p>
                    <button class="btn btn-primary" onclick="showProjetos()">
                        <i class="bi bi-plus"></i> Criar Primeiro Projeto
                    </button>
                </div>
            `;
      return;
    }

    const projectsHTML = this.recentProjects.map(projeto => `
            <div class="recent-project-item">
                <div class="row align-items-center">
                    <div class="col-md-6">
                        <h6 class="mb-1">${projeto.nome}</h6>
                        <small class="text-muted">
                            <i class="bi bi-calendar"></i> 
                            Início: ${DataFormatter.formatDate(projeto.dataInicio)}
                        </small>
                    </div>
                    <div class="col-md-3">
                        <span class="badge badge-status ${DataFormatter.getStatusClass(projeto.status)}">
                            ${DataFormatter.formatProjectStatus(projeto.status)}
                        </span>
                    </div>
                    <div class="col-md-3">
                        <div class="d-flex justify-content-end">
                            <button class="btn btn-sm btn-outline-primary me-2" 
                                    onclick="viewProjeto(${projeto.id})">
                                <i class="bi bi-eye"></i>
                            </button>
                            <button class="btn btn-sm btn-outline-secondary" 
                                    onclick="editProjeto(${projeto.id})">
                                <i class="bi bi-pencil"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `).join('');

    container.innerHTML = projectsHTML;
  }

  async refresh() {
    await this.init();
  }
}

const dashboard = new DashboardController();

function showDashboard() {
  hideAllContents();

  const dashboardContent = document.getElementById('dashboardContent');
  if (dashboardContent) {
    dashboardContent.style.display = 'block';
    dashboardContent.classList.add('fade-in');
  }

  updateNavigation('dashboard');

  dashboard.init();
}

function updateNavigation(activeSection) {
  const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
  navLinks.forEach(link => link.classList.remove('active'));

  const activeLink = document.querySelector(`[onclick*="${activeSection}"]`);
  if (activeLink) {
    activeLink.classList.add('active');
  }
}

function hideAllContents() {
  const contents = [
    'dashboardContent',
    'projetosContent',
    'pessoasContent',
    'membrosContent'
  ];

  contents.forEach(contentId => {
    const element = document.getElementById(contentId);
    if (element) {
      element.style.display = 'none';
      element.classList.remove('fade-in');
    }
  });
} 