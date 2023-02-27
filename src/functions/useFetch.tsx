import { type } from "os";
import { useEffect, useState } from "react";
import DTO from "../DTO/DTO";

const useFetch = (url: string) => {

    const [data, setData] = useState<DTO[]>([]);
    
    useEffect ( () => {
        fetch(url)
        .then ( resp => resp.json() )
        .then ( data => {
            setData(data);
        })
    },[]);

    return data;
}

export default useFetch;