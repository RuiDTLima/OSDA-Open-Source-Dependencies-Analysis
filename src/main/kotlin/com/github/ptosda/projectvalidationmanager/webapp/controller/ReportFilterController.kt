package com.github.ptosda.projectvalidationmanager.webapp.controller

import com.github.ptosda.projectvalidationmanager.database.repositories.DependencyRepository
import com.github.ptosda.projectvalidationmanager.database.repositories.LicenseRepository
import com.github.ptosda.projectvalidationmanager.database.repositories.ProjectUserRepository
import com.github.ptosda.projectvalidationmanager.websecurity.service.SecurityServiceImpl
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/")
class ReportFilterController(
                             val securityService: SecurityServiceImpl,
                             val dependencyRepo: DependencyRepository,
                             val licenseRepo: LicenseRepository,
                             val projectUserRepo: ProjectUserRepository)
{

    /**
     * HashMap with all report search functions
     */
    val searchFilterFunctions = hashMapOf<String, (String, String) -> HashMap<String, Any>>(
            "projs" to { searchText, userName ->
                hashMapOf( "projects" to projectUserRepo.findAll().filter {
                    it.pk.userInfo.username == userName && it.pk.project!!.name!!.startsWith(searchText, true)
                }.map { it.pk.project },
                        "view_name" to "project/project-list-partial"
                )
            },
            "dependencies" to { searchText, _ ->
                hashMapOf("dependencies" to dependencyRepo.findAll()
                        .groupBy { it.pk.id + it.pk.mainVersion }
                        .values
                        .map { it.last() }
                        .sortedBy{ it.pk.id }
                        .filter { it.pk.id.startsWith(searchText, true) },
                        "view_name" to "dependency/dependency-list-partial"
                )
            },
            "licenses" to { searchText, _ ->
                hashMapOf( "licenses" to licenseRepo.findAll().filter { it.spdxId.trimStart('(').startsWith(searchText, true) },
                        "view_name" to "license/generic-license-list-partial"
                )
            }
    )

    /**
     * Gets the view for a search request
     * @param searchType the type of search to be done
     * @param searchValue the value used in the search
     * @param model hashToHex map to store all view related information
     */
    @GetMapping("search/{search-type}")
    fun search(@PathVariable("search-type") searchType: String,
               @RequestParam("value") searchValue: String,
               model: HashMap<String, Any?>) : ModelAndView
    {
        val userName = securityService.findLoggedInUsername()

        if(userName == null) {
            return ModelAndView("redirect:/?error", null)
        }

        model.putAll(searchFilterFunctions[searchType]!!.invoke(searchValue, userName))

        return ModelAndView(model["view_name"].toString(), model)
    }
}