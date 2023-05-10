import { useState } from "react";
import OperationTypes from "../../OperationTypes";
import BookList, { FeedbackPopup } from "../../reusable-components/BookList";
import Feedback from "../../reusable-components/Feedback";
import SearchFilter from "./SearchFilter";
import { Operation } from "../../reusable-components/BookList"; 
import BookSortingCriteria from "../../DTO/BookSortingCriteriaDTO";
import appendParamsToUrl from "../../functions/appendParamsToUrl";
import { BOOKS_URL } from "../../constants/constants";
import _ from 'lodash';

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

    const initialUrl = BOOKS_URL + '/to-borrow'
    const [url, setUrl] = useState(initialUrl)

    const handleSearch = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault()

        if(_.isEqual(initialSearchingCriteria, searchingCriteria)) {
            setUrl(initialUrl)
        } else {
            setUrl(appendParamsToUrl(BOOKS_URL+'/criteria', new Map(Object.entries(searchingCriteria))))
        }
    }
    
    const handleReset = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, ref: React.RefObject<HTMLFormElement>) => {
        e.preventDefault()
        setUrl(initialUrl)
        ref.current?.reset()
    }

    const [renderFeedback, setRenderFeedback] = useState(false)
    const [feedbackText, setFeedbackText] = useState("")

    const handleBorrow = () => {
        if (!isAuthenticated) {
            setRenderLogInText(true);
        } else {
            fetch(BOOKS_URL + `/${bookId}/borrow`, {
                method: "POST",
                credentials: "include"
            })
            .then(resp => {
                if (resp.ok) {
                    setFeedbackText("Thank you for borrow this book!")
                    setRenderFeedback(true)

                    setTimeout(function () {
                        setRenderFeedback(false)
                    },1300);
                } else {
                    throw Error(resp.status.toString());
                }
            })
            .catch(error => {
                error.message === "409" ? setFeedbackText("You already borrowed this book!") : setFeedbackText("Something goes wrong, try again!")
                setRenderFeedback(true)

                setTimeout(function () {
                    setRenderFeedback(false)
                },1300);
            })
            
        }

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
        text: feedbackText
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