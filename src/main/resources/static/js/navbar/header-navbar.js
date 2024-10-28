document.addEventListener('DOMContentLoaded', (event) => {
    navbarCheckAdmin();
});

async function navbarCheckAdmin() {
  console.log('123123123');
  const token = sessionStorage.getItem("accessToken");
  // 우선 토큰 존재 여부 확인
  if (!token) {
//    document.getElementById('adminButton').style.display = 'none';
//    document.getElementById('cartButton').style.display = 'none';
//    document.getElementById('myButton').style.display = 'none';
  } else {
    // 관리자 토큰 여부 확인
      const res = await fetch("/api/admin/check", {
        headers: {
          'Authorization': `Bearer ${token}`
        },
      });

      const result = await res.text();
      if (result === "ADMIN") {
//        document.getElementById('adminButton').style.display = 'none';
//        document.getElementById('cartButton').style.display = 'none';
//        document.getElementById('myButton').style.display = 'none';
        return;
      } else {
//        document.getElementById('adminButton').style.display = 'none';
        document.getElementById('cartButton').style.display = 'inline';
        document.getElementById('myButton').style.display = 'inline';
      }
  }
};