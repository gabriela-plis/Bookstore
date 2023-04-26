import { type } from "os";
import { useEffect, useState } from "react";
import DTO from "../DTO/DTO";

function useFetch<Type>(url: string): Type[] {

    const [data, setData] = useState<Type[]>([]);
    
    useEffect ( () => {
        fetch(url, {
            credentials: 'include'
        })
        .then ( resp => resp.json() )
        .then ( data => {
            setData(data);
        })
    },[]);

    return data;
}

export default useFetch;