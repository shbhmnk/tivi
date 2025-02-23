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

package app.tivi.domain.interactors

import app.tivi.data.daos.FollowedShowsDao
import app.tivi.data.entities.RefreshType
import app.tivi.data.repositories.episodes.SeasonsEpisodesRepository
import app.tivi.domain.Interactor
import app.tivi.util.AppCoroutineDispatchers
import javax.inject.Inject
import kotlinx.coroutines.withContext

class UpdateUpNextEpisodes @Inject constructor(
    private val followedShowsDao: FollowedShowsDao,
    private val seasonEpisodeRepository: SeasonsEpisodesRepository,
    private val updateFollowedShows: UpdateFollowedShows,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateUpNextEpisodes.Params>() {

    override suspend fun doWork(params: Params) {
        updateFollowedShows.executeSync(
            UpdateFollowedShows.Params(
                forceRefresh = params.forceRefresh,
                type = RefreshType.QUICK,
            ),
        )

        withContext(dispatchers.io) {
            for (entry in followedShowsDao.getUpNextShows()) {
                if (params.forceRefresh ||
                    seasonEpisodeRepository.needEpisodeUpdate(entry.episode.id)
                ) {
                    seasonEpisodeRepository.updateEpisode(entry.episode.id)
                }
            }
        }
    }

    data class Params(val forceRefresh: Boolean)
}
