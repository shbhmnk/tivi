/*
 * Copyright 2018 Google LLC
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

package app.tivi.data.repositories.search

import app.tivi.data.bodyOrThrow
import app.tivi.data.entities.ShowTmdbImage
import app.tivi.data.entities.TiviShow
import app.tivi.data.mappers.TmdbShowResultsPageToTiviShows
import app.tivi.data.withRetry
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject
import retrofit2.awaitResponse

class TmdbSearchDataSource @Inject constructor(
    private val tmdb: Tmdb,
    private val mapper: TmdbShowResultsPageToTiviShows,
) : SearchDataSource {
    override suspend fun search(
        query: String,
    ): List<Pair<TiviShow, List<ShowTmdbImage>>> = withRetry {
        tmdb.searchService()
            .tv(query, 1, null, null, false)
            .awaitResponse()
            .let { mapper.map(it.bodyOrThrow()) }
    }
}
