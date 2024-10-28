//import * as Api from "../api.js";
//import { getImageUrl } from "../aws-s3.js";
import { navigate, createNavbar } from "/js/useful-functions.js";
// import {attach} from "bulma-carousel/src/js";


// 요소(element), input 혹은 상수
const sliderDiv = document.querySelector("#slider");
//const sliderArrowLeft = document.querySelector("#sliderArrowLeft");
//const sliderArrowRight = document.querySelector("#sliderArrowRight");

addAllElements();
addAllEvents();
homeCheckAdmin()

setTimeout(() => {
    homeCheckAdmin();
}, 100);

async function homeCheckAdmin() {
  console.log('qweqweqwe');
  const token = sessionStorage.getItem("accessToken");
  // 우선 토큰 존재 여부 확인
  if (token) {
    const res = await fetch("/api/admin/check", {
        headers: {
          'Authorization': `Bearer ${token}`
        },
    });

    const result = await res.text();
    if (result === "ADMIN") {
        document.getElementById('adminButton').style.display = 'inline';
        return;
    } else {
        document.getElementById('adminButton').style.display = 'none';
    }
  }
};

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
async function addAllElements() {
  createNavbar();
  await addImageCardsToSlider();
  attachSlider();
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {}

// TODO api에서 카테고리 정보 및 사진 가져와서 슬라이드 카드로 사용
async function addImageCardsToSlider() {
  const categories = [
  { name: "banner1" },
  { name: "banner2" },
  { name: "banner4" },
  { name: "banner5" },
  { name: "banner3" }
  ];

  for (const category of categories) {
    const { name } = category;

    sliderDiv.insertAdjacentHTML(
      "beforeend",
      `
      <div class="card" id="category-${name}">
          <div class="card-image">
            <figure class="image">
              <img
                src="/img/${name}.png"
                alt="카테고리 이미지"
              />
            </figure>
          </div>
      </div>
    `
    );

    const card = document.querySelector(`#category-${name}`);

    // TODO 해당 카테고리로 이동하는 기능인듯?
    //card.addEventListener("click", navigate(`/product/list?category=${name}`));
  }
}

function attachSlider() {
    const imageSlider = bulmaCarousel.attach("#slider", {
        autoplay: true,
        autoplaySpeed: 6000,
        infinite: true,
        duration: 500,
        pauseOnHover: false,
        navigation: false,
      });


    const sliderArrowLeft = document.querySelector(".slider-navigation-previous");
    const sliderArrowRight = document.querySelector(".slider-navigation-next");

    sliderArrowLeft.addEventListener("click", () => {
      imageSlider[0].previous();
    });

    sliderArrowRight.addEventListener("click", () => {
      imageSlider[0].next();
    });
};


