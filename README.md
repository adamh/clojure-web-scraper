# Clojure Web Scraper

Scrapes the [Charlottesville-Albemarle SPCA](http://caspca.org/) for their adoptable animals. Then stores the animals name, picture, and link to their details in a SQLite Database.

## Usage

* Clone this repo
* Install [Leiningen](https://leiningen.org/) (Or you can `brew install` it on OSX)
* In the repo directory run `lein run`
* Or create an UberJar with `lein uberjar`

## Scraping Quirks Explained

You may notice some not pretty code for scraping the search pages. This is because:
* An Animal may or may not have an image. If there's no image a div of a different class appears.
* Anchor tags inside the table to not have unique ids or tags so instead we grab all of them and filter out the ones that don't look like names.

## License

Copyright © 2017 Adam Hockensmith

Distributed under the MIT license.
