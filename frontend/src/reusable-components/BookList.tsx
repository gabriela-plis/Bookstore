import { useEffect, useState } from "react";
import OperationTypes from "../variable/OperationTypes";
import Popup from "./Popup";
import Pagination from "./Pagination";
import PagedBooksDTO from "../DTO/PagedBooksDTO";
import usePaginationFetch from "../functions/usePaginationFetch";
import appendParamsToUrl from "../functions/appendParamsToUrl";

export type Operation = {
    type: OperationTypes,
    handle: () => void
}

export type FeedbackPopup = {
    render: boolean,
    text: string
}

type Props = {
    url: string,
    operation: Operation,
    bookId: number,
    setBookId: React.Dispatch<React.SetStateAction<number>>,
    feedback?: FeedbackPopup
    forceUpdate?: any
  }

const BookList = (props: Props) => {
    const {url, operation, bookId, setBookId, feedback, forceUpdate} = {...props} 
    const [isOperationActive, setIsOperationActive] = useState(false);

    const [currentPage, setCurrentPage] = useState(1);

    const [pageParam, setPageParam] = useState(new Map(
        [
            ['page', 0],
        ]
    ))

    const [urlWithPagination, setUrlWithPagination] = useState(appendParamsToUrl(url, pageParam))

    const paginationResult: PagedBooksDTO = usePaginationFetch(urlWithPagination, forceUpdate);

    const handleClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, bookId: number) => {
        e.preventDefault();

        setBookId(bookId);
        setIsOperationActive(true);
    }

    const updateUrlAndScroll = () => {
        setUrlWithPagination(appendParamsToUrl(url, pageParam))
        window.scrollTo(0,0)
    }

    const paginate = (pageNumber: number) => {
        setCurrentPage(pageNumber)

        const index = pageNumber-1
        setPageParam(pageParam.set('page', index))

        updateUrlAndScroll()

    }

  useEffect(() => {
        setCurrentPage(1);

        const pageParam = new Map(
            [
                ['page', 0],
            ]
        )
     
        setUrlWithPagination(appendParamsToUrl(url, pageParam))
        window.scrollTo(0,0)
    },[url]) 

    if (paginationResult.books.length === 0) {
        return (
            <section className="book-list__container">
                <p>No books for display</p>
            </section>
        )
    }

    return (
       
        <section className="book-list__container">
            <ul className="book-list__list">
                {paginationResult.books.map( book => (
                <li className="book" key={book.id} data-key={book.id}>
                    <h2>{book.title}</h2>
                    <div className="book__details">
                        <p>Author: {book.author}</p>
                        <p>Publish year: {book.publishYear}</p>  
                        <p>Type: {book.type.name}</p>
                    </div>
                    <button className="btn" onClick={(e) => handleClick(e, book.id!)}>{capitalize(operation.type)}</button>
                    {isOperationActive && bookId === book.id && <Popup title={operation.type} book={book} handleOperation={operation.handle} setIsOperationActive={setIsOperationActive}/>}
                   {feedback?.render &&  
                    <div className="popup__background">
                        <section className="popup__container">
                            <p>{feedback.text}</p>
                        </section>
                    </div>
                    }
                </li>
                ))}
            </ul>
            <Pagination totalPages={paginationResult.totalPages} paginate={paginate} currentPage={currentPage}/>
        </section> 
     );

     
}

const capitalize = (word: string) => {
    return word.charAt(0).toUpperCase() + word.slice(1);
}
 
export default BookList;