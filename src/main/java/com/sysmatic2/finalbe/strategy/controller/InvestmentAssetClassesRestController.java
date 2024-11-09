package com.sysmatic2.finalbe.strategy.controller;

import com.sysmatic2.finalbe.strategy.dto.InvestmentAssetClassesDto;
import com.sysmatic2.finalbe.strategy.dto.InvestmentAssetClassesPayloadDto;
import com.sysmatic2.finalbe.strategy.service.InvestmentAssetClassesService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin InvestmentAssetClasses Controller", description = "관리자가 투자자산 분류를 관리하는 컨트롤러")
public class InvestmentAssetClassesRestController {
    private final InvestmentAssetClassesService iacService;

//    1. 투자자산분류 목록
    @GetMapping(value="/inv-asset-classes", produces="application/json")
    @ApiResponse(responseCode="200", description = "List of Investment Asset Classes")
    @ApiResponse(responseCode="400", description = "Wrong Request URL")
    @ApiResponse(responseCode="401", description = "Unauthorized")
    @ApiResponse(responseCode="405", description = "Wrong Request Method")
    @ApiResponse(responseCode="500", description = "Other Errors")
    public ResponseEntity<Map> getAllInvestmentAssetClasses(@RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) throws Exception{
        List<InvestmentAssetClassesDto> dtoList = iacService.getList(page, pageSize);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("data", dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }


    //1-1. 투자자산분류 상세
    @GetMapping(value="/inv-asset-classes/{id}", produces="application/json")
    @ApiResponse(responseCode="200", description = "Return Investment Asset Classes find by Id")
    @ApiResponse(responseCode="400", description = "Wrong Request URL")
    @ApiResponse(responseCode="401", description = "Unauthorized")
    @ApiResponse(responseCode="404", description = "NOT EXIST")
    @ApiResponse(responseCode="405", description = "Wrong Request Method")
    @ApiResponse(responseCode="500", description = "Other Errors")
    public ResponseEntity<Map> getInvestmentAssetClasses(@PathVariable("id") Integer id) throws Exception {
        InvestmentAssetClassesDto iasDto = iacService.getById(id);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("data", iasDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }

    //2. 투자자산분류 등록
    @PostMapping(value="/inv-asset-classes", consumes = "application/json", produces="application/json")
    @ApiResponse(responseCode="201", description = "Register inv asset classes")
    @ApiResponse(responseCode="400", description = "Wrong Request URL")
    @ApiResponse(responseCode="401", description = "Unauthorized")
    @ApiResponse(responseCode="405", description = "Wrong Request Method")
    @ApiResponse(responseCode="500", description = "Other Errors")
    public ResponseEntity<Map> addInvestmentAssetClass(@Valid @RequestBody InvestmentAssetClassesPayloadDto iacPayloadDto) throws Exception {
        //데이터 저장
        iacService.register(iacPayloadDto);

        //해쉬맵에 성공 메시지 저장
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("msg", "CREATE_SUCCESS");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
    }

    //3. 투자자산분류 삭제
//    @DeleteMapping(value="/inv-asset-classes/{id}")
//    @ApiResponse(responseCode="200", description = "Delete Investment Asset Classes by Id")
//    @ApiResponse(responseCode="400", description = "Wrong Request URL")
//    @ApiResponse(responseCode="401", description = "Unauthorized")
//    @ApiResponse(responseCode="404", description = "NOT EXIST")
//    @ApiResponse(responseCode="405", description = "Wrong Request Method")
//    @ApiResponse(responseCode="500", description = "Other Errors")
//    public ResponseEntity<Map> deleteInvestmentAssetClass(@PathVariable("id") Integer id) throws Exception {
//        iacService.delete(id);
//        Map<String, String> responseMap = new HashMap<>();
//        responseMap.put("msg", "DELETE_SUCCESS");
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
//    }

    //3-1. 투자자산분류 soft delete
    @DeleteMapping(value="/inv-asset-classes/{id}")
    @ApiResponse(responseCode="200", description = "Delete Investment Asset Classes by Id")
    @ApiResponse(responseCode="400", description = "Wrong Request URL")
    @ApiResponse(responseCode="401", description = "Unauthorized")
    @ApiResponse(responseCode="404", description = "NOT EXIST")
    @ApiResponse(responseCode="405", description = "Wrong Request Method")
    @ApiResponse(responseCode="500", description = "Other Errors")
    public ResponseEntity<Map> deleteInvestmentAssetClass(@PathVariable("id") Integer id) throws Exception {
        iacService.softDelete(id);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("msg", "DELETE_SUCCESS");

        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }

    //4. 투자자산분류 수정
    @PutMapping(value="/inv-asset-classes/{id}")
    @ApiResponse(responseCode="200", description = "Delete Investment Asset Classes by Id")
    @ApiResponse(responseCode="400", description = "Wrong Request URL")
    @ApiResponse(responseCode="401", description = "Unauthorized")
    @ApiResponse(responseCode="404", description = "NOT EXIST")
    @ApiResponse(responseCode="405", description = "Wrong Request Method")
    @ApiResponse(responseCode="500", description = "Other Errors")
    public ResponseEntity<Map> updateInvestmentAssetClass(@PathVariable("id") Integer id,
                                                          @Valid @RequestBody InvestmentAssetClassesPayloadDto iacPayloadDto) throws Exception {

        iacService.update(id, iacPayloadDto);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("msg", "UPDATE_SUCCESS");

        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }
}
