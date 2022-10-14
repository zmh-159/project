package main

import (
	"encoding/base64"
	"fmt"
	"time"
	"io/ioutil"
	"net/http"
)

func main() {
	fmt.Println("\u001B[2J\u001B[H")
	for{		
		req, _ := http.NewRequest("GET", "http://192.168.31.183:8001/api/cmdRun/sendCmd?cmd=netStat", nil)
		req.Header.Set("authorization", "Basic "+base64.StdEncoding.EncodeToString([]byte("root:root")))
		resp, err := (&http.Client{}).Do(req)
		if err != nil {
			fmt.Println(err)
		}
		defer resp.Body.Close()
		body, err := ioutil.ReadAll(resp.Body)
		res := string(body)
		fmt.Println(res)
		time.Sleep(time.Second*2)
	}
	
}

