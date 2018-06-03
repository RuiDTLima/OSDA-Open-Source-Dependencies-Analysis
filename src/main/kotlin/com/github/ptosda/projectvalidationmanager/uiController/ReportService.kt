package com.github.ptosda.projectvalidationmanager.uiController

import com.github.ptosda.projectvalidationmanager.database.entities.*
import org.springframework.stereotype.Service

@Service
class ReportService
{
    /**
     * Gets an HashMap with all with both vulnerable and not vulnerable dependencies
     * @param report the report to search for dependencies
     */
    fun getBuildDependencies(report: Report): Map<String, List<Any>> {
        val vulnerable = arrayListOf<Any>()
        val notVulnerable = arrayListOf<Any>()

        report.dependency?.forEach {
            if(it.vulnerabilitiesCount == 0) {
                notVulnerable.add(it)
            }
            else {
                vulnerable.add(it)
            }
        }

        return hashMapOf("vulnerable_dependencies" to vulnerable,
                         "dependencies" to notVulnerable)
    }

}
