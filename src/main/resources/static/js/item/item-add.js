import { addImageToS3 } from "/js/aws-s3.js";
import * as Api from "/js/api.js";
import { checkLogin, randomId, createNavbar, checkAdmin } from "/js/useful-functions.js";

// 요소(element)들과 상수들
const titleInput = document.querySelector("#titleInput");
const categorySelectBox = document.querySelector("#categorySelectBox");
const manufacturerInput = document.querySelector("#manufacturerInput");
const shortDescriptionInput = document.querySelector("#shortDescriptionInput");
const detailDescriptionInput = document.querySelector(
  "#detailDescriptionInput"
);
const imageInput = document.querySelector("#imageInput");
const inventoryInput = document.querySelector("#inventoryInput");
const priceInput = document.querySelector("#priceInput");
const searchKeywordInput = document.querySelector("#searchKeywordInput");
const addKeywordButton = document.querySelector("#addKeywordButton");
const keywordsContainer = document.querySelector("#keywordContainer");
const submitButton = document.querySelector("#submitButton");
const registerItemForm = document.querySelector("#registerItemForm");
const fileNameSpan = document.querySelector("#fileNameSpan"); // 파일 이름 표시 요소

//TODO 아이템 등록을 위해 로그인 비활성화
checkLogin();
//checkAdmin();
addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
  addOptionsToSelectBox();
}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  imageInput.addEventListener("change", handleImageUpload);
  submitButton.addEventListener("click", handleSubmit);
  categorySelectBox.addEventListener("change", handleCategoryChange);
  addKeywordButton.addEventListener("click", handleKeywordAdd);
}

// 제품 추가 - 사진은 AWS S3에 저장, 이후 제품 정보를 백엔드 db에 저장.
async function handleSubmit(e) {
  e.preventDefault();

  const title = titleInput.value;
  const categoryId = categorySelectBox.value;
  const manufacturer = manufacturerInput.value;
  const shortDescription = shortDescriptionInput.value;
  const detailDescription = detailDescriptionInput.value;
  const image = imageInput.files[0];
  const inventory = parseInt(inventoryInput.value);
  const price = parseInt(priceInput.value);

  // 입력 칸이 비어 있으면 진행 불가
  if (!title || !manufacturer || !shortDescription || !detailDescription || !inventory || !price) {
    return alert("빈 칸 및 0이 없어야 합니다.");
  }
  if (!image) {
        return alert("사진을 넣어주세요.");
  }

  if (image.size > 3e6) {
    return alert("사진은 최대 2.5MB 크기까지 가능합니다.");
  }

  if(categoryId==="default"){
      return alert("카테고리를 선택해 주세요.");
  }

  try {
    console.log("이미지 및 데이터 업로드 시작...");

    // FormData 생성 및 데이터 추가
    const formData = new FormData();
    formData.append("title", title);
    formData.append("categoryId", categoryId);
    formData.append("manufacturer", manufacturer);
    formData.append("shortDescription", shortDescription);
    formData.append("detailDescription", detailDescription);
    formData.append("imageKey", image); // 파일 추가
    formData.append("inventory", inventory);
    formData.append("price", price);
    // json 배열타입 그대로 전송
    formData.append("keyWords", searchKeywords);


    // FormData 전송
    await fetch("/item/item-add", {
      method: "POST",
      body: formData,
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`
      }
    });

    alert(`정상적으로 ${title} 제품이 등록되었습니다.`);

    // 폼 초기화
    registerItemForm.reset();
    // 셀렉트박스 초기화
    categorySelectBox.options.value="default";
    fileNameSpan.innerText = "";
    keywordsContainer.innerHTML = "";
    searchKeywords = [];
    // 부모 창에 reload 메시지 전송
    window.opener.postMessage("reloadPage", "*");
    // 현재 창 닫기
    window.close();
  } catch (err) {
    console.log("등록 오류:", err.stack);
    alert(`문제가 발생하였습니다. 확인 후 다시 시도해 주세요: ${err.message}`);
  }
}

document.addEventListener('DOMContentLoaded', function () {
    // 상품 등록 후 모달창 종료 처리
    window.addEventListener("message", (event) => {
        if (event.data === "reloadPage") {
            location.reload();
        }
    });
});

// 사용자가 사진을 업로드했을 때, 파일 이름이 화면에 나타나도록 함.
function handleImageUpload() {
  const file = imageInput.files[0];
  if (file) {
    fileNameSpan.innerText = file.name;
  } else {
    fileNameSpan.innerText = "";
  }
}

// 선택할 수 있는 카테고리 종류를 api로 가져와서, 옵션 태그를 만들어 삽입함.
async function addOptionsToSelectBox() {
  categorySelectBox.innerHTML = `<option value="default" selected disabled hidden>카테고리를 선택해 주세요.</option>`; // 기본값 추가
  const categories = await Api.get("/category/category-list");
  categories.forEach((category) => {
    const { id, title, themeClass } = category;
    categorySelectBox.insertAdjacentHTML(
      "beforeend",
      `<option value=${id} class="notification ${themeClass}"> ${title} </option>`
    );
  });
}


// 카테고리 선택 시, 선택박스에 해당 카테고리 테마가 반영되게 함.
function handleCategoryChange() {
  const index = categorySelectBox.selectedIndex;

  categorySelectBox.className = categorySelectBox[index].className;
}

// 아래 함수는, 검색 키워드 추가 시, 해당 키워드로 만든 태그가 화면에 추가되도록 함.
// 아래 배열은, 나중에 api 요청 시 사용함.
let searchKeywords = [];
function handleKeywordAdd(e) {
  e.preventDefault();

  const newKeyword = searchKeywordInput.value;

  if (!newKeyword) {
    return;
  }

  if (searchKeywords.includes(newKeyword)) {
    return alert("이미 추가한 검색어입니다.");
  }

  searchKeywords.push(newKeyword);

  const random = randomId();

  keywordsContainer.insertAdjacentHTML(
    "beforeend",
    `
    <div class="control" id="a${random}">
      <div class="tags has-addons">
        <span class="tag is-link is-light">${newKeyword}</span>
        <a class="tag is-link is-light is-delete"></a>
      </div>
    </div>
  `
  );

  // x 버튼에 삭제 기능 추가.
  keywordsContainer
    .querySelector(`#a${random} .is-delete`)
    .addEventListener("click", handleKeywordDelete);

  // 초기화 및 사용성 향상
  searchKeywordInput.value = "";
  searchKeywordInput.focus();
}

function handleKeywordDelete(e) {
  // a 태그 클릭 -> 옆의 span 태그의 inenerText가 키워드임.
  const keywordToDelete = e.target.previousElementSibling.innerText;

  // 배열에서 삭제
  const index = searchKeywords.indexOf(keywordToDelete);
  searchKeywords.splice(index, 1);

  // 요소 삭제
  e.target.parentElement.parentElement.remove();
}

//console.log("JavaScript 로드 성공");
//// 요소들 가져오기
//const titleInput = document.querySelector("#titleInput");
//const manufacturerInput = document.querySelector("#manufacturerInput");
//const shortDescriptionInput = document.querySelector("#shortDescriptionInput");
//const detailDescriptionInput = document.querySelector("#detailDescriptionInput");
//const imageInput = document.querySelector("#imageInput");
//const inventoryInput = document.querySelector("#inventoryInput");
//const priceInput = document.querySelector("#priceInput");
//const searchKeywordInput = document.querySelector("#searchKeywordInput");
//const addKeywordButton = document.querySelector("#addKeywordButton");
//const keywordsContainer = document.querySelector("#keywordContainer");
//const submitButton = document.querySelector("#submitButton");
//const registerItemForm = document.querySelector("#registerItemForm");
//const fileNameSpan = document.querySelector("#fileNameSpan");
//
//// 검색 키워드를 저장할 배열
//let searchKeywords = [];
//
//// 이벤트 리스너 등록
//imageInput.addEventListener("change", handleImageUpload);
//addKeywordButton.addEventListener("click", handleKeywordAdd);
//submitButton.addEventListener("click", handleSubmit);
//
//// 파일 업로드 시 파일 이름 표시
//function handleImageUpload() {
//    const file = imageInput.files[0];
//    fileNameSpan.textContent = file ? file.name : "사진 파일 (png, jpg, jpeg)";
//}
//
//// 검색 키워드 추가
//function handleKeywordAdd() {
//    const newKeyword = searchKeywordInput.value.trim();
//    if (newKeyword && !searchKeywords.includes(newKeyword)) {
//        searchKeywords.push(newKeyword);
//        const keywordElement = document.createElement("span");
//        keywordElement.textContent = newKeyword;
//        keywordElement.classList.add("tag");
//        keywordsContainer.appendChild(keywordElement);
//        searchKeywordInput.value = "";
//    }
//}
//
//// 폼 제출
//async function handleSubmit() {
//  // 서버로 보낼 데이터를 준비합니다.
//  const data = {
//    title: titleInput.value,
//    manufacturer: manufacturerInput.value,
//    shortDescription: shortDescriptionInput.value,
//    detailDescription: detailDescriptionInput.value,
//    imageKey: imageInput.value,
//    inventory: inventoryInput.value,
//    price: priceInput.value,
//    searchKeywords,
//  };
//
//  try {
//    const response = await fetch("/item/item-add", {
//      method: "POST",
//      headers: {
//        "Content-Type": "application/json",
//      },
//      body: JSON.stringify(data),
//    });
//
//    if (response.ok) {
//      alert("상품이 성공적으로 등록되었습니다.");
//      window.location.href = "/item/item-list";
//    } else {
//      alert("상품 등록에 실패했습니다.");
//    }
//  } catch (error) {
//    console.error("Error:", error);
//    alert("오류가 발생했습니다.");
//  }
//}
