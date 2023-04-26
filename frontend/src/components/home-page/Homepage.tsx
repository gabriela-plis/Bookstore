import { useEffect, useState } from "react";
import OperationTypes from "../../OperationTypes";
import BookList from "../../reusable-components/BookList";
import Feedback from "../../reusable-components/Feedback";
import Popup from "../../reusable-components/Popup";
import SearchFilter from "./SearchFilter";
import { Operation } from "../../reusable-components/BookList";

type Props = {
    isAuthenticated: boolean,
    displayLogoutText: boolean;
}

const Homepage = (props: Props) => {
    const {isAuthenticated, displayLogoutText} = {...props};
    const [displayLogInText, setDisplayLogInText] = useState(false);

    const [bookId, setBookId] = useState(0);

    const handleBorrow = () => {
        console.log(isAuthenticated)
        //not logged in
        if (!isAuthenticated) {
            setDisplayLogInText(true);
        }

        //you already have this book borrowed
        //borrow
    }

    const operation: Operation = {
        type: OperationTypes.Borrow,
        handle: handleBorrow
    }

//  http://localhost:8080/books/to-borrow

    return ( 
    <main className="homepage">
        <h2 className="homepage__title">Book To Borrow:</h2>
        {displayLogoutText && 
            <Feedback text="You have successfully been logged out! Have a nice day" button/>
        }
        {!isAuthenticated &&
            <Feedback text="Log In to borrow a book!" button={false}/>
        }
        <BookList url='http://localhost:8080/books/to-borrow'  bookId={bookId} setBookId={setBookId} />
        <SearchFilter />
    </main> 
    );
}

export default Homepage;