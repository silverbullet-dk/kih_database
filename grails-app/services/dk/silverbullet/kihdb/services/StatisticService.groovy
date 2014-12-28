/**
 * Copyright 2014 Stiftelsen for Software-baserede Sundhedsservices - 4S
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.silverbullet.kihdb.services

import dk.silverbullet.kihdb.model.LaboratoryReport
import dk.silverbullet.kihdb.model.SelfMonitoringSample
import dk.silverbullet.kihdb.model.Statistic
import grails.transaction.Transactional

@Transactional
class StatisticService {

    def getCreators() {
        SelfMonitoringSample.createCriteria().list {
            projections {
                distinct("createdByText")
            }
        }
    }

    /**
     * Borgere<br />
     * select count(distinct citizen_id) from self_monitoring_sample where created_by_text = 'TeleCare Nord'
     */
    def getCitizens(String creator) {
        SelfMonitoringSample.createCriteria().list {
            eq("createdByText", creator)
            projections {
                countDistinct("citizen")
            }
        }[0]
    }

    /**
     * Målingsgrupper<br />
     * select count(distinct id) from self_monitoring_sample where created_by_text = 'TeleCare Nord'
     */
    def getMeasurementGroups(String creator) {
        SelfMonitoringSample.createCriteria().list {
            eq("createdByText", creator)
            projections {
                countDistinct("id")
            }
        }[0]
    }

    /**
     * Målinger<br />
     * select count(distinct id) from laboratory_report where sample_id in (select id from self_monitoring_sample where created_by_text = 'TeleCare Nord')
     */
    def getMeasurements(String creator) {
        LaboratoryReport.executeQuery("select count(distinct id) from LaboratoryReport where sample.id in " +
                "(select id from SelfMonitoringSample where createdByText = ?)", [creator])[0]
    }

    void createStatistic() {
        Date statisticTime = new Date()
        getCreators().each {
            new Statistic(statisticType: 'Borgere', statisticTime: statisticTime, creator: it, value: getCitizens(it)).save()
            new Statistic(statisticType: 'Målingsgrupper', statisticTime: statisticTime, creator: it, value: getMeasurementGroups(it)).save()
            new Statistic(statisticType: 'Målinger', statisticTime: statisticTime, creator: it, value: getMeasurements(it)).save()
        }
    }
}
