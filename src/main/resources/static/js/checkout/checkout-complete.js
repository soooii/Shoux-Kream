import { checkLogin, navigate, createNavbar } from "/js/useful-functions.js";

// 요소(element), input 혹은 상수
const checkOutDetailButton = document.querySelector("#checkOutDetailButton");
const shoppingButton = document.querySelector("#shoppingButton");

//TODO 유저 로그인 기능이 결합되면 정리할 것
checkLogin();
addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  checkOutDetailButton.addEventListener("click", navigate("/users/me/purchase"));
  shoppingButton.addEventListener("click", navigate("/"));
}
