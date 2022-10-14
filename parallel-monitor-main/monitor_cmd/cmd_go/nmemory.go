package main

import (
	"encoding/base64"
	"fmt"
	"io/ioutil"
	"net/http"
)

func main() {
	req, _ := http.NewRequest("GET", "http://192.168.31.183:8001/api/cmdRun/sendCmd?cmd=memoryStatic", nil)
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

