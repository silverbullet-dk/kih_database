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
class UrlMappings {

    static mappings = {
        "/login/$action?"(controller: "login")
        "/logout/$action?"(controller: "logout")

        // Default one
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        //For the meta controller
        "/currentVersion"(controller: "meta", action: "currentServerVersion")
        "/isAlive"(controller: "meta", action: "isAlive")
        "/isAlive/html"(controller: "meta", action: "isAlive")
        "/isAlive/json"(controller: "meta", action: "isAliveJSON")
        "/noAccess"(controller: "meta", action: "noAccess")


        "/"(controller: "home", action: "index")

        "500"(view: '/error')
    }
}
