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

@TestFor(SelfMonitoringSampleController)
@Mock(SelfMonitoringSample)
class SelfMonitoringSampleControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/selfMonitoringSample/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.selfMonitoringSampleInstanceList.size() == 0
        assert model.selfMonitoringSampleInstanceTotal == 0
    }


    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/selfMonitoringSample/list'

//        populateValidParams(params)
//        def selfMonitoringSample = new SelfMonitoringSample(params)
//
//        assert selfMonitoringSample.save() != null
//
//        params.id = selfMonitoringSample.id
//
//        def model = controller.show()
//
//        assert model.selfMonitoringSampleInstance == selfMonitoringSample
    }

}
