# Web Crawler
## Comenzi
`crawler crawl <fisier_configurare> <fisier_url>`

`crawler search <path> <maxFileSize> <keywords>`

`crawler list <path> <keyword>` unde keyword este extensie

## Fisierele de configurare
### config.txt
Este utilizat pentru `URLDownloader` si va contine:
```
  {nume de director care va fi creat si in care se vor salva siteurile}
  {nr de pagini ce vor fi downloadate}
  {numarul de threaduri pe care va fi rulat programul}
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
