import { useEffect, useState } from "react";

function useFetch<Type>(url: string, forceUpdate?: any): Type[] {

    const [data, setData] = useState<Type[]>([]);
    
    useEffect ( () => {
        fetch(url, {
            credentials: 'include'
        })
        .then ( resp => resp.json() )
        .then ( data => {
            setData(data);
        })
    },[url, forceUpdate]);

    return data;
}

export default useFetch;