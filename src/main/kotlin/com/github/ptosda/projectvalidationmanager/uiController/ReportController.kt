package com.github.ptosda.projectvalidationmanager.uiController

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import kotlin.collections.HashMap
import kotlin.collections.set

@Controller
@RequestMapping("/report")
class ReportController(val provider: UiProvider) {

    /**
     * Get the latest build reports received. Show projects and add a filter ( group, repository )
     */
    @GetMapping
    fun getHome(model: HashMap<String, Any>) : String{

        model["page_title"] = "Home"

        model["builds"] = provider.provideLatestBuilds()

        model["projects"] = provider.provideLatestProjects()

        return "index"
    }


    /**
     * Tem que se verificar a chave primaria de projecto pois pode haver com nomes iguais
     */
    @GetMapping("/build/{project-name}")
    fun getProjectBuilds(@RequestParam("project-name", required = false) projectName: String?,
                         model: HashMap<String, Any>) : String{

        model["page_title"] = "Home"

        model["builds"] = provider.provideLatestBuilds()

        model["projects"] = provider.provideLatestProjects()

        return "index"
    }

    /**
     * Get the details of a build ( Dependencies, Licenses and Vulnerabilities )
     *
     * TODO Highlight dependencies that have vulnerabilities, by showing them in a different list than the rest for example
     *
     */
    @GetMapping("build/{project-name}/{timestamp}/detail")
    fun getBuildDetail(@RequestParam("project-name", required = false) projectName: String?,
                       @RequestParam("timestamp", required = false) timestamp: String?,
                       model: HashMap<String, Any>) : String
    {
        model["page_title"] = "BuildDetail"

        val buildInfo = provider.provideBuildDetail()

        model["project_name"] = buildInfo.project_name
        model["project_version"] = buildInfo.project_version
        model["timestamp"] = buildInfo.timestamp
        model["tag"] = buildInfo.tag
        model["dependencies"] = buildInfo.dependencies

        return "build-detail"
    }

    /**
     * Get the details of a dependency ( Dependencies, Licenses and Vulnerabilities )
     */
    @GetMapping("dependency/{id}/{version}/detail")
    fun getDependencyDetail(@RequestParam("id", required = false) dependencyId: String?,
                            @RequestParam("version", required = false) version: String?,
                            model: HashMap<String, Any>) : String
    {
        model["page_title"] = "DependencyDetail"

        return "dependency-detail"
    }

}