package com.feidian.sidebarTree.controller;

import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.sidebarTree.domain.GermplasmParents;
import com.feidian.sidebarTree.service.GermplasmParentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * GermplasmParentsController
 *
 * @author Coder-RSL
 * @date 2023-07-01
 */
@RestController
@RequestMapping("/germplasmparents")
public class GermplasmParentsController extends BaseController {
    @Autowired
    private GermplasmParentsService germplasmParentsService;

    @GetMapping("/{id}")
    public AjaxResult getGermplasmParentsById(@PathVariable Long id) {
        return AjaxResult.success(germplasmParentsService.getGermplasmParentsById(id));
    }
    @GetMapping("/find")
    public AjaxResult findGermplasmParents(GermplasmParents germplasmParents) {
         List<GermplasmParents> lists =  germplasmParentsService.findGermplasmParents(germplasmParents);
        return AjaxResult.success(lists);
    }

    @GetMapping
    public AjaxResult getAllGermplasmParents() {

        return AjaxResult.success(germplasmParentsService.getAllGermplasmParents());
    }

    @PostMapping
    public AjaxResult createGermplasmParents(@RequestBody GermplasmParents germplasmParents) {
        
        return AjaxResult.success(germplasmParentsService.createGermplasmParents(germplasmParents));
    }

    @PutMapping
    public AjaxResult updateGermplasmParents(@RequestBody GermplasmParents germplasmParents) {
        return AjaxResult.success(germplasmParentsService.updateGermplasmParents(germplasmParents));
    }

    @DeleteMapping("/{id}")
    public AjaxResult deleteGermplasmParents(@PathVariable Long id) {
        return AjaxResult.success(germplasmParentsService.deleteGermplasmParents(id));
    }
}
