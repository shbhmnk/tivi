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

package app.tivi.data.mappers

import app.tivi.data.entities.TiviShow
import com.uwetrottmann.tmdb2.entities.TvShow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbShowToTiviShow @Inject constructor() : Mapper<TvShow, TiviShow> {
    override suspend fun map(from: TvShow) = TiviShow(
        tmdbId = from.id,
        imdbId = from.external_ids?.imdb_id,
        title = from.name,
        summary = from.overview,
        homepage = from.homepage,
        network = from.networks?.firstOrNull()?.name,
        networkLogoPath = from.networks?.firstOrNull()?.logo_path,
    )
}
