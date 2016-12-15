/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.performance

import org.gradle.performance.categories.BasicPerformanceTest
import org.junit.experimental.categories.Category
import spock.lang.Unroll

import static org.gradle.performance.results.Flakiness.flaky
import static org.gradle.performance.results.Flakiness.not_flaky

@Category(BasicPerformanceTest)
class DependencyReportPerformanceTest extends AbstractCrossVersionPerformanceTest {
    @Unroll("Project '#testProject' dependency report")
    def "dependency report"() {
        given:
        runner.testId = "dependencyReport $testProject"
        runner.testProject = testProject
        runner.tasksToRun = ['dependencyReport']
        runner.targetVersions = targetVersions

        when:
        def result = runner.run(flakiness)

        then:
        result.assertCurrentVersionHasNotRegressed(flakiness)

        where:
        testProject       | targetVersions              | flakiness
        "small"           | ['3.2-20161004202618+0000'] | not_flaky
        "multi"           | ['3.2-20161004202618+0000'] | not_flaky
        // TODO(pepper): Revert this to 'last' when 3.2 is released
        // The regression was determined acceptable in this discussion:
        // https://issues.gradle.org/browse/GRADLE-1346
        "lotDependencies" | ['3.2-20161004202618+0000'] | flaky
    }
}