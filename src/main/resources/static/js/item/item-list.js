//import { getImageUrl } from "/js/aws-s3.js";
//import * as Api from "/js/api.js";
//import {
//  randomId,
//  getUrlParams,
//  addCommas,
//  navigate,
//  checkUrlParams,
//  createNavbar,
//} from "/js/useful-functions.js";
//
//// 요소(element), input 혹은 상수
//const ItemContainer = document.querySelector("#itemItemContainer");
//
//checkUrlParams("category");
//addAllElements();
//addAllEvents();
//
//// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
//function addAllElements() {
//  createNavbar();
//  addItemItemsToContainer();
//}
//
//// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
//function addAllEvents() {}
//
//async function addItemItemsToContainer() {
//  const { category } = getUrlParams();
//  console.log(category)
//  const items = await Api.get(`/item/lists?categoryTitle=${category}`);
//
//  for (const item of items) {
//    // 객체 destructuring
//    const { id, title, shortDescription, imageKey, isRecommended, price } =
//      item;
//    const imageUrl = await getImageUrl(imageKey);
//    const random = randomId();
//
//    itemItemContainer.insertAdjacentHTML(
//      "beforeend",
//      `
//      <div class="message media item-item" id="a${random}">
//        <div class="media-left">
//          <figure class="image">
//            <img
//              src="${imageUrl}"
//              alt="제품 이미지"
//            />
//          </figure>
//        </div>
//        <div class="media-content">
//          <div class="content">
//            <p class="title">
//              ${title}
//              ${
//                isRecommended
//                  ? '<span class="tag is-success is-rounded">추천</span>'
//                  : ""
//              }
//            </p>
//            <p class="description">${shortDescription}</p>
//            <p class="price">${addCommas(price)}원</p>
//          </div>
//        </div>
//      </div>
//      `
//    );
//
//    const itemItem = document.querySelector(`#a${random}`);
//    itemItem.addEventListener(
//      "click",
//      navigate(`/item/detail?id=${id}`)
//    );
//  }
//import * as Api from "/js/api.js";
import {
  randomId,
  getUrlParams,
  addCommas,
  navigate,
  checkUrlParams,
  createNavbar,
} from "/js/useful-functions.js";

// 요소(element), input 혹은 상수
const itemItemContainer = document.querySelector("#itemItemContainer");

checkUrlParams("category");
addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
  addItemItemsToContainer();
}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {}

async function addItemItemsToContainer() {
  const { category } = getUrlParams();
  console.log(category);
  const items = await Api.get(`/item/lists?categoryTitle=${category}`);

  for (const item of items) {
    // 객체 destructuring
    const { id, title, shortDescription, isRecommended, price } = item;
    const imageUrl = "/img/default-item.png"; // 기본 이미지 경로를 사용

    const random = randomId();

    itemItemContainer.insertAdjacentHTML(
      "beforeend",
      `
      <div class="message media item-item" id="a${random}">
        <div class="media-left">
          <figure class="image">
            <img
              src="${imageUrl}"
              alt="제품 이미지"
            />
          </figure>
        </div>
        <div class="media-content">
          <div class="content">
            <p class="title">
              ${title}
              ${
                isRecommended
                  ? '<span class="tag is-success is-rounded">추천</span>'
                  : ""
              }
            </p>
            <p class="description">${shortDescription}</p>
            <p class="price">${addCommas(price)}원</p>
          </div>
        </div>
      </div>
      `
    );

    const itemItem = document.querySelector(`#a${random}`);
    itemItem.addEventListener("click", () => {
      navigate(`/item/detail?id=${id}`);
    });
  }
}
