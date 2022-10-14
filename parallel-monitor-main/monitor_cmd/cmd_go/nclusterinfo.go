package main

import (
	"encoding/base64"
	"fmt"
	"os"
	"time"
	"io/ioutil"
	"net/http"
)

func main() {
	var url = "http://192.168.31.183:8001/api/cmdRun/sendCmd?cmd=clusterInfo"
	if len(os.Args)>1 {
			switch(os.Args[1]){
			case "-lc":
				url = "http://192.168.31.183:8001/api/cmdRun/sendCmd?cmd=clusterInfo+-lc"
				break;
			case "-lm":
				url = "http://192.168.31.183:8001/api/cmdRun/sendCmd?cmd=clusterInfo+-lm"
				break;
			case "-ld":
				url = "http://192.168.31.183:8001/api/cmdRun/sendCmd?cmd=clusterInfo+-ld"
				break;
			case "-lcmd":
                                url = "http://192.168.31.183:8001/api/cmdRun/sendCmd?cmd=clusterInfo+-lcmd"
                                break;
			}
		}
	fmt.Println("\u001B[2J\u001B[H")
	for{	
		req, _ := http.NewRequest("GET", url, nil)
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
