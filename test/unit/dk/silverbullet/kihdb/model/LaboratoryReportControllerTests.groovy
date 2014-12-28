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
package dk.silverbullet.kihdb.model

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(LaboratoryReportController)
@Mock(LaboratoryReport)
class LaboratoryReportControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'

    }


    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/laboratoryReport/list'

//        populateValidParams(params)
//        def laboratoryReport = new LaboratoryReport(params)
//
//        assert laboratoryReport.save() != null
//
//        params.id = laboratoryReport.id
//
//        def model = controller.show()
//
//        assert model.laboratoryReportInstance == laboratoryReport
    }

}
