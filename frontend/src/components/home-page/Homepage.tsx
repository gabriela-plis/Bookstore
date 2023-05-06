import {useState } from "react";
import OperationTypes from "../../OperationTypes";
import BookList, { FeedbackPopup } from "../../reusable-components/BookList";
import Feedback from "../../reusable-components/Feedback";
import SearchFilter from "./SearchFilter";
import { Operation } from "../../reusable-components/BookList"; 
import BookSortingCriteria from "../../DTO/BookSortingCriteriaDTO";
import appendParamsToUrl from "../../functions/appendParamsToUrl";

type Props = {
    isAuthenticated: boolean,
    renderLogoutText: boolean,
    setRenderLogoutText: React.Dispatch<React.SetStateAction<boolean>>;
}

const Homepage = (props: Props) => {
    const {isAuthenticated, renderLogoutText, setRenderLogoutText} = {...props};

    const [renderLogInText, setRenderLogInText] = useState(!isAuthenticated);
    
    const [bookId, setBookId] = useState(0);

    const currentYear = new Date().getFullYear()
    const initialSearchingCriteria: BookSortingCriteria = {
        minPublishYear: 1950, 
        maxPublishYear: currentYear,
        types: new Set<string>()
    }

    const [searchingCriteria, setSearchingCriteria] = useState<BookSortingCriteria>(initialSearchingCriteria)

    const initialUrl = "http://localhost:8080/books/to-borrow"
    const [url, setUrl] = useState(initialUrl)

    const handleSearch = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault()

        if(searchingCriteria.types?.size === 0) {
            setUrl(initialUrl)
        } else {
            setUrl(appendParamsToUrl("http://localhost:8080/books/criteria", new Map(Object.entries(searchingCriteria))))
        }
    }
    
    const handleReset = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault()
        setUrl(initialUrl)
    }

    const [renderFeedback, setRenderFeedback] = useState(false)

    const handleBorrow = () => {
        if (!isAuthenticated) {
            setRenderLogInText(true);
        } else {
            fetch(`http://localhost:8080/books/${bookId}/borrow`, {
                method: "POST",
                credentials: "include"
            })
            .then(resp => {
                if (resp.status === 200) {
                    setRenderFeedback(true)

                    setTimeout(function () {
                        setRenderFeedback(false)
                    },1500);
    
                }
            })
            
        }

        //you already have this book borrowed
        //borrow
    }

    const handleCloseFeedback = () => {
        setRenderLogoutText(false)
        setRenderLogInText(true)
    }


    const operation: Operation = {
        type: OperationTypes.Borrow,
        handle: handleBorrow
    }

    const feedback: FeedbackPopup = {
        render: renderFeedback,
        text: "Thank you for borrow this book!"
    }


    return ( 
    <main className="homepage">
        <div className="homepage__wrapper-top-section">
            <h2 className="homepage__title">Book To Borrow:</h2>
            {renderLogoutText && 
                <Feedback text="You have successfully been logged out! Have a nice day" button handleClick={handleCloseFeedback}/>
            }
            {renderLogInText && !renderLogoutText &&
                <Feedback text="Log In to borrow a book!" button={false} />
            }
        </div>
        <BookList url={url} bookId={bookId} setBookId={setBookId} operation={operation} feedback={feedback}/>
        <SearchFilter setSearchingCriteria={setSearchingCriteria} handleReset={handleReset} handleSearch={handleSearch} />
    </main> 
    );
}

export default Homepage;