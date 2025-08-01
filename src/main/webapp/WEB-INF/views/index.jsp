<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <!DOCTYPE html>
  <html lang="pt-BR">

  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Portfolio Manager - Sistema de Gestão de Projetos</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
  </head>

  <body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container">
        <a class="navbar-brand" href="#">
          <i class="bi bi-kanban"></i> Portfolio Manager
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <a class="nav-link active" href="#" onclick="showDashboard()">
                <i class="bi bi-house"></i> Dashboard
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#" onclick="showProjetos()">
                <i class="bi bi-folder"></i> Projetos
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#" onclick="showPessoas()">
                <i class="bi bi-people"></i> Pessoas
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#" onclick="showMembros()">
                <i class="bi bi-person-badge"></i> Membros
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <!-- Main Content -->
    <div class="container-fluid mt-4">
      <div id="mainContent">
        <!-- Dashboard Content -->
        <div id="dashboardContent">
          <div class="row">
            <div class="col-12">
              <h1 class="mb-4">
                <i class="bi bi-speedometer2"></i> Dashboard
              </h1>
            </div>
          </div>

          <!-- Statistics Cards -->
          <div class="row mb-4">
            <div class="col-md-3">
              <div class="card text-white bg-primary">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <div>
                      <h4 class="card-title">Total Projetos</h4>
                      <h2 id="totalProjetos">0</h2>
                    </div>
                    <div class="align-self-center">
                      <i class="bi bi-folder fs-1"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-3">
              <div class="card text-white bg-success">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <div>
                      <h4 class="card-title">Projetos Ativos</h4>
                      <h2 id="projetosAtivos">0</h2>
                    </div>
                    <div class="align-self-center">
                      <i class="bi bi-check-circle fs-1"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-3">
              <div class="card text-white bg-warning">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <div>
                      <h4 class="card-title">Total Pessoas</h4>
                      <h2 id="totalPessoas">0</h2>
                    </div>
                    <div class="align-self-center">
                      <i class="bi bi-people fs-1"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-3">
              <div class="card text-white bg-info">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <div>
                      <h4 class="card-title">Total Membros</h4>
                      <h2 id="totalMembros">0</h2>
                    </div>
                    <div class="align-self-center">
                      <i class="bi bi-person-badge fs-1"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Recent Projects -->
          <div class="row">
            <div class="col-12">
              <div class="card">
                <div class="card-header">
                  <h5><i class="bi bi-clock-history"></i> Projetos Recentes</h5>
                </div>
                <div class="card-body">
                  <div id="recentProjectsList">
                    <!-- Projects will be loaded here -->
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Projects Content -->
        <div id="projetosContent" style="display: none;">
          <div class="row">
            <div class="col-12">
              <div class="d-flex justify-content-between align-items-center mb-4">
                <h1><i class="bi bi-folder"></i> Gestão de Projetos</h1>
                <button class="btn btn-primary" onclick="showProjetoModal()">
                  <i class="bi bi-plus"></i> Novo Projeto
                </button>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <div class="card">
                <div class="card-body">
                  <div class="table-responsive">
                    <table class="table table-striped" id="projetosTable">
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Nome</th>
                          <th>Gerente</th>
                          <th>Data Início</th>
                          <th>Status</th>
                          <th>Risco</th>
                          <th>Orçamento</th>
                          <th>Ações</th>
                        </tr>
                      </thead>
                      <tbody id="projetosTableBody">
                        <!-- Projects will be loaded here -->
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- People Content -->
        <div id="pessoasContent" style="display: none;">
          <div class="row">
            <div class="col-12">
              <div class="d-flex justify-content-between align-items-center mb-4">
                <h1><i class="bi bi-people"></i> Gestão de Pessoas</h1>
                <button class="btn btn-primary" onclick="showPessoaModal()">
                  <i class="bi bi-plus"></i> Nova Pessoa
                </button>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <div class="card">
                <div class="card-body">
                  <div class="table-responsive">
                    <table class="table table-striped" id="pessoasTable">
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Nome</th>
                          <th>CPF</th>
                          <th>Data Nascimento</th>
                          <th>Funcionário</th>
                          <th>Gerente</th>
                          <th>Ações</th>
                        </tr>
                      </thead>
                      <tbody id="pessoasTableBody">
                        <!-- People will be loaded here -->
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Members Content -->
        <div id="membrosContent" style="display: none;">
          <div class="row">
            <div class="col-12">
              <div class="d-flex justify-content-between align-items-center mb-4">
                <h1><i class="bi bi-person-badge"></i> Gestão de Membros</h1>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <div class="card">
                <div class="card-body">
                  <div class="table-responsive">
                    <table class="table table-striped" id="membrosTable">
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Pessoa</th>
                          <th>Projeto</th>
                          <th>Data Início</th>
                          <th>Data Fim</th>
                          <th>Ações</th>
                        </tr>
                      </thead>
                      <tbody id="membrosTableBody">
                        <!-- Members will be loaded here -->
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modals -->
    <!-- Projeto Modal -->
    <div class="modal fade" id="projetoModal" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="projetoModalTitle">Novo Projeto</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <form id="projetoForm">
              <div class="row">
                <div class="col-md-6">
                  <div class="mb-3">
                    <label for="projetoNome" class="form-label">Nome do Projeto *</label>
                    <input type="text" class="form-control" id="projetoNome" required>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="mb-3">
                    <label for="projetoGerente" class="form-label">Gerente Responsável *</label>
                    <select class="form-select" id="projetoGerente" required>
                      <option value="">Selecione um gerente</option>
                    </select>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <div class="mb-3">
                    <label for="projetoDataInicio" class="form-label">Data de Início *</label>
                    <input type="date" class="form-control" id="projetoDataInicio" required>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="mb-3">
                    <label for="projetoPrevisaoTermino" class="form-label">Previsão de Término</label>
                    <input type="date" class="form-control" id="projetoPrevisaoTermino">
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <div class="mb-3">
                    <label for="projetoStatus" class="form-label">Status *</label>
                    <select class="form-select" id="projetoStatus" required>
                      <option value="">Selecione o status</option>
                      <option value="EM_ANALISE">Em Análise</option>
                      <option value="ANALISE_REALIZADA">Análise Realizada</option>
                      <option value="ANALISE_APROVADA">Análise Aprovada</option>
                      <option value="INICIADO">Iniciado</option>
                      <option value="PLANEJADO">Planejado</option>
                      <option value="EM_ANDAMENTO">Em Andamento</option>
                      <option value="ENCERRADO">Encerrado</option>
                      <option value="CANCELADO">Cancelado</option>
                    </select>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="mb-3">
                    <label for="projetoRisco" class="form-label">Risco *</label>
                    <select class="form-select" id="projetoRisco" required>
                      <option value="">Selecione o risco</option>
                      <option value="BAIXO">Baixo</option>
                      <option value="MEDIO">Médio</option>
                      <option value="ALTO">Alto</option>
                    </select>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <div class="mb-3">
                    <label for="projetoOrcamento" class="form-label">Orçamento Total</label>
                    <input type="number" class="form-control" id="projetoOrcamento" step="0.01">
                  </div>
                </div>
              </div>
              <div class="mb-3">
                <label for="projetoDescricao" class="form-label">Descrição</label>
                <textarea class="form-control" id="projetoDescricao" rows="3"></textarea>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
            <button type="button" class="btn btn-primary" onclick="saveProjeto()">Salvar</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Pessoa Modal -->
    <div class="modal fade" id="pessoaModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="pessoaModalTitle">Nova Pessoa</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <form id="pessoaForm">
              <div class="mb-3">
                <label for="pessoaNome" class="form-label">Nome *</label>
                <input type="text" class="form-control" id="pessoaNome" required>
              </div>
              <div class="mb-3">
                <label for="pessoaCpf" class="form-label">CPF</label>
                <input type="text" class="form-control" id="pessoaCpf">
              </div>
              <div class="mb-3">
                <label for="pessoaDataNascimento" class="form-label">Data de Nascimento</label>
                <input type="date" class="form-control" id="pessoaDataNascimento">
              </div>
              <div class="row">
                <div class="col-md-6">
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="pessoaFuncionario">
                    <label class="form-check-label" for="pessoaFuncionario">
                      Funcionário
                    </label>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="pessoaGerente">
                    <label class="form-check-label" for="pessoaGerente">
                      Gerente
                    </label>
                  </div>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
            <button type="button" class="btn btn-primary" onclick="savePessoa()">Salvar</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Membro Modal -->
    <div class="modal fade" id="membroModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="membroModalTitle">Novo Membro</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <form id="membroForm">
              <div class="mb-3">
                <label for="membroPessoa" class="form-label">Pessoa *</label>
                <select class="form-select" id="membroPessoa" required>
                  <option value="">Selecione uma pessoa</option>
                </select>
              </div>
              <div class="mb-3">
                <label for="membroProjeto" class="form-label">Projeto *</label>
                <select class="form-select" id="membroProjeto" required>
                  <option value="">Selecione um projeto</option>
                </select>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <div class="mb-3">
                    <label for="membroDataInicio" class="form-label">Data de Início *</label>
                    <input type="date" class="form-control" id="membroDataInicio" required>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="mb-3">
                    <label for="membroDataFim" class="form-label">Data de Fim</label>
                    <input type="date" class="form-control" id="membroDataFim">
                  </div>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
            <button type="button" class="btn btn-primary" onclick="saveMembro()">Salvar</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/resources/js/api.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/dashboard.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/projetos.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/pessoas.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/membros.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/app.js"></script>
  </body>

  </html>