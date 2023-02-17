import { type } from "os";
import { useEffect, useState } from "react";
import DTO from "./DTO/DTO";

const useFetch = (url: string) => {

    const [data, setData] = useState<DTO[]>([]);
    console.log(url);
    
    useEffect ( () => {
        fetch(url)
        .then ( resp => resp.json() )
        .then ( data => {
            console.log(data)
            setData(data);
        })
    },[]);

    console.log("useFetch method:");
    console.log(data);

    return data;
}

export default useFetch;