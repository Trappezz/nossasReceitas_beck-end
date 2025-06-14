function toggleSenha() {
  const input = document.getElementById("senha");
  input.type = input.type === "password" ? "text" : "password";
}

// Lógica de login simulada
document.getElementById("formLogin").addEventListener("submit", function (event) {
  event.preventDefault();

  const email = document.getElementById("email").value.trim();
  const senha = document.getElementById("senha").value.trim();
  const erroDiv = document.getElementById("mensagemErro");

  if (!email || !senha) {
    erroDiv.textContent = "Preencha todos os campos.";
    return;
  }

  // Simulação de verificação
  if (email === "admin@empresa.com" && senha === "1234") {
    erroDiv.style.color = "green";
    erroDiv.textContent = "Login bem-sucedido!";
    // Redirecionar ou continuar...
    // window.location.href = "pagina-protegida.html";
  } else {
    erroDiv.style.color = "red";
    erroDiv.textContent = "E-mail ou senha inválidos.";
  }

  fetch("http://localhost:8080/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, senha })
  })
});
