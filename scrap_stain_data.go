package main

import (
	"bufio"
	"database/sql"
	"fmt"
	"github.com/PuerkitoBio/goquery"
	_ "github.com/go-sql-driver/mysql"
	"os"
	"path/filepath"
	"strings"
)

func trim(s string) string {
	return strings.Trim(s, " \n\t\r")
}
func insert(con *sql.DB, from string, on string, need string, steps string, notes string, img string) {
	_, e := con.Exec("insert into stain (`from`, `on`, need, steps, notes, img) value (?,?,?,?,?,?)", trim(from), trim(on), trim(need), trim(steps), trim(notes), img)
	if e != nil {

		panic(e)
	}
}

func main() {

	con, e1 := sql.Open("mysql", "root@/stain")
	if e1 != nil {
		panic(e1)
	}
	matches, err := filepath.Glob("st*.html")
	if err != nil {
		panic(err)
	}

	//fmt.Printf("%s", matches)
	for _, match := range matches {
		f, _ := os.Open(match)
		r := bufio.NewReader(f)
		doc, e := goquery.NewDocumentFromReader(r)
		if e != nil {
			panic(e)
		}
		from := doc.Find("h1").Text()
		img, _ := doc.Find("#stainImgDiv").Find("img").Attr("src")
		img = img[7:]
		doc.Find(".whitebox").Each(func(i int, s *goquery.Selection) {
			title := s.Find("h2").Text()
			fmt.Printf("Title: %s\n", title)
			need, e := s.Find("ul").Html()
			steps, e := s.Find("ol").Html()
			notes, e := s.Find("div").Html()
			if e != nil {
				panic(e)
			}
			insert(con, from, title, need, steps, notes, img)
		})
	}

}
