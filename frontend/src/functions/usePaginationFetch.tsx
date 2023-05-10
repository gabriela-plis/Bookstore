import { useEffect, useState } from "react";
import PagedBooksDTO from "../DTO/PagedBooksDTO";

function usePaginationFetch<Type>(url: string): PagedBooksDTO {

    const [data, setData] = useState<PagedBooksDTO>({
        totalPages: 0,
        books: []
    });
    
    useEffect ( () => {
        fetch(url, {
            credentials: 'include'
        })
        .then ( resp => resp.json() )
        .then ( data => {
            setData(data);
        })
    },[url]);

    return data;
}

export default usePaginationFetch;