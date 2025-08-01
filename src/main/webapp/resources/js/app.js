class App {
  constructor() {
    this.currentSection = 'dashboard';
  }

  async init() {
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', () => this.setupApp());
    } else {
      this.setupApp();
    }
  }

  async setupApp() {
    try {
      this.setupGlobalEvents();

      showDashboard();


    } catch (error) {
      NotificationService.showError('Erro ao inicializar aplicação');
    }
  }

  setupGlobalEvents() {
    document.addEventListener('click', (e) => {
      if (e.target.matches('a[href="#"]')) {
        e.preventDefault();
      }
    });

    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
      return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
      return new bootstrap.Popover(popoverTriggerEl);
    });
    this.setupFormValidation();
  }

  setupFormValidation() {
    document.addEventListener('input', (e) => {
      if (e.target.hasAttribute('required')) {
        this.validateField(e.target);
      }
    });
    document.addEventListener('blur', (e) => {
      if (e.target.hasAttribute('required')) {
        this.validateField(e.target);
      }
    }, true);
  }

  validateField(field) {
    const isValid = field.checkValidity();

    if (isValid) {
      field.classList.remove('is-invalid');
      field.classList.add('is-valid');
    } else {
      field.classList.remove('is-valid');
      field.classList.add('is-invalid');
    }
  }

  static showGlobalLoading() {
    LoadingService.show();
  }
  static hideGlobalLoading() {
    LoadingService.hide();
  }

  static showSuccess(message) {
    NotificationService.showSuccess(message);
  }
  static showError(message) {
    NotificationService.showError(message);
  }

  static showWarning(message) {
    NotificationService.showWarning(message);
  }
  static showInfo(message) {
    NotificationService.showInfo(message);
  }
}

const app = new App();

document.addEventListener('DOMContentLoaded', () => {
  app.init();
});
function showLoading() {
  App.showGlobalLoading();
}

function hideLoading() {
  App.hideGlobalLoading();
}
function showSuccess(message) {
  App.showSuccess(message);
}

function showError(message) {
  App.showError(message);
}

function showWarning(message) {
  App.showWarning(message);
}
function showInfo(message) {
  App.showInfo(message);
}
function formatDate(dateString) {
  return DataFormatter.formatDate(dateString);
}


function formatCurrency(value) {
  return DataFormatter.formatCurrency(value);
}
function formatProjectStatus(status) {
  return DataFormatter.formatProjectStatus(status);
}
function formatProjectRisk(risk) {
  return DataFormatter.formatProjectRisk(risk);
}

function getStatusClass(status) {
  return DataFormatter.getStatusClass(status);
}

function getRiskClass(risk) {
  return DataFormatter.getRiskClass(risk);
}

async function apiGet(endpoint) {
  return api.get(endpoint);
}
async function apiPost(endpoint, data) {
  return api.post(endpoint, data);
}
async function apiPut(endpoint, data) {
  return api.put(endpoint, data);
}

async function apiDelete(endpoint) {
  return api.delete(endpoint);
}

window.App = App;
window.app = app;
window.showLoading = showLoading;
window.hideLoading = hideLoading;
window.showSuccess = showSuccess;
window.showError = showError;
window.showWarning = showWarning;
window.showInfo = showInfo;
window.formatDate = formatDate;
window.formatCurrency = formatCurrency;
window.formatProjectStatus = formatProjectStatus;
window.formatProjectRisk = formatProjectRisk;
window.getStatusClass = getStatusClass;
window.getRiskClass = getRiskClass;
window.apiGet = apiGet;
window.apiPost = apiPost;
window.apiPut = apiPut;
window.apiDelete = apiDelete; 