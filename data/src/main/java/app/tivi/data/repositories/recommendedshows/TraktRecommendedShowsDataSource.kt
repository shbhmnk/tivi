/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.tivi.data.repositories.recommendedshows

import app.tivi.data.bodyOrThrow
import app.tivi.data.entities.TiviShow
import app.tivi.data.mappers.TraktShowToTiviShow
import app.tivi.data.mappers.forLists
import app.tivi.data.withRetry
import com.uwetrottmann.trakt5.services.Recommendations
import javax.inject.Inject
import javax.inject.Provider
import retrofit2.awaitResponse

class TraktRecommendedShowsDataSource @Inject constructor(
    private val recommendationsService: Provider<Recommendations>,
    private val showMapper: TraktShowToTiviShow,
) {
    suspend operator fun invoke(page: Int, pageSize: Int): List<TiviShow> = withRetry {
        recommendationsService.get()
            // We add 1 because Trakt uses a 1-based index whereas we use a 0-based index
            .shows(page + 1, pageSize, null)
            .awaitResponse()
            .let { showMapper.forLists().invoke(it.bodyOrThrow()) }
    }
}
