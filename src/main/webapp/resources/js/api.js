
class ApiService {
  constructor() {
    this.baseUrl = '/api';
    this.headers = {
      'Content-Type': 'application/json',
    };
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseUrl}${endpoint}`;
    const config = {
      headers: this.headers,
      ...options
    };

    try {
      const response = await fetch(url, config);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      if (response.status === 204) {
        return null;
      }

      return await response.json();
    } catch (error) {
      console.error('API Error:', error);
      throw error;
    }
  }

  async get(endpoint) {
    return this.request(endpoint, { method: 'GET' });
  }

  async post(endpoint, data) {
    return this.request(endpoint, {
      method: 'POST',
      body: JSON.stringify(data)
    });
  }

  async put(endpoint, data) {
    return this.request(endpoint, {
      method: 'PUT',
      body: JSON.stringify(data)
    });
  }
  async delete(endpoint) {
    return this.request(endpoint, { method: 'DELETE' });
  }

  async getProjetos() {
    return this.get('/projetos');
  }

  async getProjeto(id) {
    return this.get(`/projetos/${id}`);
  }

  async createProjeto(projeto) {
    return this.post('/projetos', projeto);
  }

  async updateProjeto(projeto) {
    return this.put('/projetos', projeto);
  }

  async deleteProjeto(projeto) {
    return this.delete('/projetos', projeto);
  }

  async getPessoas() {
    return this.get('/pessoas');
  }
  async getPessoa(id) {
    return this.get(`/pessoas/${id}`);
  }

  async createPessoa(pessoa) {
    return this.post('/pessoas', pessoa);
  }

  async updatePessoa(pessoa) {
    return this.put('/pessoas', pessoa);
  }

  async deletePessoa(pessoa) {
    return this.delete('/pessoas', pessoa);
  }
  async getMembros() {
    return this.get('/membros');
  }

  async getMembro(id) {
    return this.get(`/membros/${id}`);
  }
}

const api = new ApiService();

class DataFormatter {
  static formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
  }

  static formatCurrency(value) {
    if (!value) return '-';
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  }

  static formatProjectStatus(status) {
    const statusMap = {
      'EM_ANALISE': 'Em Análise',
      'ANALISE_REALIZADA': 'Análise Realizada',
      'ANALISE_APROVADA': 'Análise Aprovada',
      'INICIADO': 'Iniciado',
      'PLANEJADO': 'Planejado',
      'EM_ANDAMENTO': 'Em Andamento',
      'ENCERRADO': 'Encerrado',
      'CANCELADO': 'Cancelado'
    };
    return statusMap[status] || status;
  }

  static formatProjectRisk(risk) {
    const riskMap = {
      'BAIXO': 'Baixo',
      'MEDIO': 'Médio',
      'ALTO': 'Alto'
    };
    return riskMap[risk] || risk;
  }


  static getStatusClass(status) {
    const classMap = {
      'EM_ANALISE': 'em-analise',
      'ANALISE_REALIZADA': 'analise-realizada',
      'ANALISE_APROVADA': 'analise-aprovada',
      'INICIADO': 'iniciado',
      'PLANEJADO': 'planejado',
      'EM_ANDAMENTO': 'em-andamento',
      'ENCERRADO': 'encerrado',
      'CANCELADO': 'cancelado'
    };
    return classMap[status] || 'default';
  }


  static getRiskClass(risk) {
    const classMap = {
      'BAIXO': 'baixo',
      'MEDIO': 'medio',
      'ALTO': 'alto'
    };
    return classMap[risk] || 'default';
  }
}

class NotificationService {

  static showSuccess(message) {
    this.showToast(message, 'success');
  }


  static showError(message) {
    this.showToast(message, 'danger');
  }

  static showWarning(message) {
    this.showToast(message, 'warning');
  }


  static showInfo(message) {
    this.showToast(message, 'info');
  }

  static showToast(message, type = 'info') {
    let container = document.querySelector('.toast-container');
    if (!container) {
      container = document.createElement('div');
      container.className = 'toast-container';
      document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast align-items-center text-white bg-${type} border-0`;
    toast.setAttribute('role', 'alert');
    toast.setAttribute('aria-live', 'assertive');
    toast.setAttribute('aria-atomic', 'true');

    toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;

    container.appendChild(toast);

    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();

    toast.addEventListener('hidden.bs.toast', () => {
      container.removeChild(toast);
    });
  }
}

class LoadingService {

  static show() {
    const overlay = document.createElement('div');
    overlay.className = 'loading-overlay';
    overlay.id = 'loadingOverlay';

    overlay.innerHTML = `
            <div class="loading-spinner">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Carregando...</span>
                </div>
                <div class="mt-2">Carregando...</div>
            </div>
        `;

    document.body.appendChild(overlay);
  }

  static hide() {
    const overlay = document.getElementById('loadingOverlay');
    if (overlay) {
      overlay.remove();
    }
  }
} 