package main
import "C"
import (
	"encoding/base64"
	"fmt"
	"io/ioutil"
	"net/http"
)
//export go_http
func go_http(value string) {
	req, _ := http.NewRequest("GET", value, nil)
	req.Header.Set("authorization", "Basic "+base64.StdEncoding.EncodeToString([]byte("root:root")))
	resp, err := (&http.Client{}).Do(req)
	if err != nil {
		fmt.Println(err)
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	res := string(body)
	fmt.Println(res)
}
func main() {//main函数是必须的 有main函数才能让cgo编译器去把包编译成C的库
}