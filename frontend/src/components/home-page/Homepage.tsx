import { useState } from "react";
import OperationTypes from "../../OperationTypes";
import BookList, { FeedbackPopup } from "../../reusable-components/BookList";
import Feedback from "../../reusable-components/Feedback";
import SearchFilter from "./SearchFilter";
import { Operation } from "../../reusable-components/BookList"; 
import BookSortingCriteria from "../../DTO/BookSortingCriteriaDTO";

type Props = {
    isAuthenticated: boolean,
    displayLogoutText: boolean;
}

const Homepage = (props: Props) => {
    const {isAuthenticated, displayLogoutText} = {...props};
    const [displayLogInText, setDisplayLogInText] = useState(false);

    const [bookId, setBookId] = useState(0);

    const currentYear = new Date().getFullYear()
    const initialSearchingCriteria: BookSortingCriteria = {
        minPublishYear: 1950, 
        maxPublishYear: currentYear,
        types: new Set<string>()
    }

    const [searchingCriteria, setSearchingCriteria] = useState<BookSortingCriteria>(initialSearchingCriteria)

    const [url, setUrl] = useState("http://localhost:8080/books/to-borrow")

    const replaceSpaces = function(word: string){
        return word.replace(/\s+/g, "+")
      }


    const handleSearch = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault()

        if(searchingCriteria.types?.size === 0) {
            setUrl("http://localhost:8080/books/to-borrow")
        } else {
            setUrl("http://localhost:8080/books/criteria".concat('?', "types=", Array.from(searchingCriteria.types!).map(replaceSpaces).join(","), "&", "min=", searchingCriteria.minPublishYear!.toString(), "&", "max=", searchingCriteria.maxPublishYear!.toString()))
        }
    }
    
    const handleReset = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault()
        setUrl("http://localhost:8080/books/to-borrow")
    }

    const [renderFeedback, setRenderFeedback] = useState(false)

    const handleBorrow = () => {
        if (!isAuthenticated) {
            setDisplayLogInText(true);
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
        <h2 className="homepage__title">Book To Borrow:</h2>
        {displayLogoutText && 
            <Feedback text="You have successfully been logged out! Have a nice day" button/>
        }
        {!isAuthenticated &&
            <Feedback text="Log In to borrow a book!" button={false}/>
        }
        <BookList url={url} bookId={bookId} setBookId={setBookId} operation={operation} feedback={feedback}/>
        <SearchFilter setSearchingCriteria={setSearchingCriteria} handleReset={handleReset} handleSearch={handleSearch} />
    </main> 
    );
}

export default Homepage;