MusicGen
========

A little Java project to convert a(n English language) string into a series of tones/music.

Please note that the algorithm uses the distribution of letters in English to convert a string into a subset of the C major scale, so providing gibberish will probably not produce as good of a result as proper English words.

Also the timing is rather arbitrary and is based upon letter weights from the game [Scrabble](http://www.hasbro.com/scrabble/en_US/).  Again, using random letters may produce unexpected tone durations.