package com.shoux_kream.checkout.controller;

import com.shoux_kream.checkout.dto.CheckOutRequestDto;
import com.shoux_kream.checkout.dto.CheckOutResponseDto;
import com.shoux_kream.checkout.service.CheckOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckOutApiController {
    private final CheckOutService checkOutService;

    //TODO UserId를 얻어서 checkout 전체 정보 반환 정보 조회용 api,
    // 페이지를 바꿀 필요가 없는가 yes
    // UserID => session 정보 변환 필수
    @GetMapping("/{userId}")
    public ResponseEntity<List<CheckOutResponseDto>> getCheckOut(@RequestParam("userId") Long id){
        List<CheckOutResponseDto> checkOutResponseDto = checkOutService.getCheckOuts(id);
        return ResponseEntity.ok(checkOutResponseDto);
    }

    //TODO checkout id를 얻어서 checkout 세부 정보 반환 => 프론트에서 js로 정보를 요청해서 받음, 동일한 기능을 프론트 view에서 해야함
    // 수정하자
    @GetMapping("/{checkOutId}")
    public String getCheckOutDetail(@RequestParam Long id){
        Model model = null;
        model.addAttribute(checkOutService.getCheckOut(id));
        CheckOutResponseDto checkOutResponseDto = new CheckOutResponseDto();
        //혹은 url에서 id값 박아넣어서
        return "checkout/checkoutcomplete";
    }

    //전체 주문 등록
    @PostMapping
    public ResponseEntity<CheckOutResponseDto> saveCheckOut(@RequestBody CheckOutRequestDto checkOutRequestDto){

        CheckOutResponseDto checkOutResponseDto = checkOutService.save(checkOutRequestDto);
        return ResponseEntity.ok(checkOutResponseDto);
    }

    @PatchMapping
    public ResponseEntity<CheckOutResponseDto> patchCheckOut(@RequestBody CheckOutRequestDto checkOutRequestDto){
        Model model = null;
        // user id 를 얻어서 카트를 얻음
        CheckOutResponseDto checkOutResponseDto = new CheckOutResponseDto();
        //혹은 url에서 id값 박아넣어서
        return ResponseEntity.ok(checkOutResponseDto);
    }
    @DeleteMapping
    public ResponseEntity<CheckOutResponseDto> deleteCheckOut(@RequestBody CheckOutRequestDto checkOutRequestDto){
        Model model = null;
        // user id 를 얻어서 카트를 얻음
        CheckOutResponseDto checkOutResponseDto = new CheckOutResponseDto();
        //혹은 url에서 id값 박아넣어서
        return ResponseEntity.ok(checkOutResponseDto);
    }

    /*
    주문내역을 확인해서 json값으로 반환하는 컨트롤러
    @RequestMapping(value = "/read/{userid}", method = RequestMethod.GET)
	public String read(@PathVariable("userid") String userid, Model model) {

		MemberVO vo = memberService.read(userid);
		model.addAttribute("userInfo", vo);

        List<OrderDTO> orderList = orderService.myOrderList(userid);
		model.addAttribute("orderList", orderList);
//        주문 내역을 list로 가져와서 view로 넘겨준다
		return "member/read";

     */
}
