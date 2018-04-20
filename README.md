# bibgen
Composes a BibTeX bibliography for all DBLP citation keys in a TeX file.

To use the tool, make sure to run [Java](https://java.com/nl/download/).
Then run the command
```
java -jar bibgen.jar yourfile.tex >> autorefs.bib
```
where `yourfile.tex` is a TeX file, and `refs.bib` is the generated bibliography.

In your latex file, you can then use the generated references along with other refences `yourrefs.bib` via
``` 
\bibliography{autorefs,yourrefs}
```
