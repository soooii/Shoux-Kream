import * as Api from "/js/api.js";
import { checkLogin, createNavbar } from "/js/useful-functions.js";

// 요소(element), input 혹은 상수
const titleInput = document.querySelector("#titleInput");
const descriptionInput = document.querySelector("#descriptionInput");
const themeSelectBox = document.querySelector("#themeSelectBox");
const imageInput = document.querySelector("#imageInput");
const fileNameSpan = document.querySelector("#fileNameSpan");
const submitButton = document.querySelector("#editCategoryButton");
const editCategoryForm = document.querySelector("#editCategoryForm");

checkLogin();
addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
async function addAllElements() {
  createNavbar();
  await loadCategoryData();
  submitButton.addEventListener("click", handleSubmit);
  themeSelectBox.addEventListener("change", handleColorChange);
  imageInput.addEventListener("change", handleImageUpload);
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  submitButton.addEventListener("click", handleSubmit);
  themeSelectBox.addEventListener("change", handleColorChange);
  imageInput.addEventListener("change", handleImageUpload);
}

// 카테고리 데이터를 로드하는 함수
async function loadCategoryData() {
  const categoryId = window.location.pathname.split("/").pop();

  try {
    const data = await Api.get(`/category/${categoryId}`);
    titleInput.value = data.title;
    descriptionInput.value = data.description;
    themeSelectBox.value = data.themeClass;
    const selectedOption = themeSelectBox.options[themeSelectBox.selectedIndex];
    const backgroundColor = selectedOption.style.backgroundColor;
    const color = selectedOption.style.color;
    themeSelectBox.style.backgroundColor = backgroundColor;
    themeSelectBox.style.color = color;

    // 이미지 파일명 표시 (해당 파일이 서버에 저장되어 있을 경우)
    fileNameSpan.innerText = `현재 사진: ${data.imageUrl}`;
  } catch (err) {
    console.error(err.stack);
    alert(`카테고리 정보를 불러오는 중 문제가 발생했습니다: ${err.message}`);
  }
}

// 카테고리 수정하기 - 사진은 AWS S3에 저장, 이후 카테고리 정보를 백엔드 db에 저장.
async function handleSubmit(e) {
    e.preventDefault();

    const title = titleInput.value;
    const description = descriptionInput.value;
    const themeClass = themeSelectBox.value;
    const image = imageInput.files[0];

    if (!title || !description) {
        return alert("빈 칸이 없어야 합니다.");
    }

    if (themeClass==="default") {
        return alert("테마를 선택해 주세요.");
    }

    if (!fileNameSpan.innerText){
        if (!image) {
                return alert("사진을 넣어주세요.");
        }
    }
    if(image){
        if (image.size > 3e6) {
                return alert("사진은 최대 2.5MB 크기까지 가능합니다.");
            }
    }

    // FormData 객체를 생성해 multipart/form-data 전송
    const formData = new FormData();
    formData.append("title", title);
    formData.append("description", description);
    formData.append("themeClass", themeClass);
    if (image) {
        formData.append("image", image);
    }

    const categoryId = window.location.pathname.split("/").pop();
    console.log(categoryId);

    try {
    await fetch(`/category/${categoryId}`, {
      method: "PATCH",
      body: formData,
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`
      }
    });

    alert("카테고리 수정이 완료되었습니다.");
    themeSelectBox.style.backgroundColor = "white";
    themeSelectBox.style.color = "black";
    window.close();
    } catch (err) {
        console.error(err.stack);
        alert(`카테고리 수정 중 문제가 발생했습니다: ${err.message}`);
    }
}

// 색상 선택 시, 선택박스에 해당 색상 반영되게 함.
function handleColorChange() {
  const index = themeSelectBox.selectedIndex;

  themeSelectBox.style.color = themeSelectBox[index].style.color;
  themeSelectBox.style.backgroundColor = themeSelectBox[index].style.backgroundColor;
}
// 사용자가 사진을 업로드했을 때, 파일 이름이 화면에 나타나도록 함.
function handleImageUpload() {
  const file = imageInput.files[0];
  if (file) {
    fileNameSpan.innerText = file.name;
  } else {
    fileNameSpan.innerText = "";
  }
}
