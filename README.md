# popularmovies - stage 1
Android Developer Nanodegree

- Show movies from TheMovieDB API;
- Allow user to select 'Popular', 'Top Rated', 'Now Playing', 'Upcoming' movies;
- Display movie details upon user select.

## Used libraries:
- Retrofit
- GSON
- Picasso
- ButterKnife
- EndlessRecyclerViewScrollListener (from https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView adapted to app)

## Notes:
- TheMovieDB api key must be provided via String Resource (R.string.tmdb_api_key)
- Despite project overview states that "Allow your user to change sort order via a setting" I decided to allow user select movie category directly from Navigation Drawer since going through a Settings page (using Settings API) would required user to 'click' more 2 ou 3 times.
