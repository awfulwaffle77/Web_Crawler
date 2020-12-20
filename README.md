# Web Crawler
## Comenzi
`crawler crawl config.txt url.txt`

`crawler search <path> <maxFileSize> <keywords>` unde path este calea locala unde se va cauta

`crawler list <path> <keyword>` unde keyword este extensie

## Fisierele de configurare
### config.txt
Este utilizat pentru `URLDownloader` si va contine:
```
  {nume de director care va fi creat si in care se vor salva siteurile}
  {nr de pagini ce vor fi downloadate}
  {numarul de threaduri pe care va fi rulat programul}
```
### url.txt
Va contine numele siteurile de pe care se va descarca (ex. `https://www.yourhtmlsource.com/`)
``` 
  {nume site 1}
  .
  .
  {nume site n}
```
### robots.txt
Va contine:
```
  contains:{lista de cuvinte separate prin virgula}
  max_lines:{numar maxim de linii}
```

## Folosirea PageInterpreter

Se va folosi membrul `pageInterpreter` din clasa `URLDownloader` pentru a apela metodele `isPageCorrect` si `formatPages` acolo unde este necesar. La momentul terminarii proiectului,
este folosita numai metoda `isPageCorrect`, desi au fost testate ambele metode. 

## Comanda Search
search <path> <maxFileSize> <keywords>
Comanda este folosita atat pentru a filtra dupa cuvinte cheie, cat si dupa dimensiune. 
In cazul in care <maxFileSize> =0 si nu sunt mentionate <keywords>, sunt salvate intr-un nou folder "filteredByCrawlerIlona" toate fisierele (nu tine cont de dimensiune). Daca <maxFileSize> > 0, se vor salva fisierele ce au o dimensiune mai mica decat <maxFileSize>.
In cazul in care sunt mentionate <keywords> si <maxFileSize> > 0, cautarea cuvintelor se executa doar in fisierele ce au dimensiunea mai mica decat <maxFileSize> si se vor salva toate fisierele ce contin cel putin un cuvant cheie din lista.

