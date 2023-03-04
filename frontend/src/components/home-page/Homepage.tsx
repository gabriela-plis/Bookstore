import { useEffect, useState } from "react";
import OperationTypes from "../../OperationTypes";
import BookList from "../../reusable-components/BookList";
import Feedback from "../../reusable-components/Feedback";
import Popup from "../../reusable-components/Popup";
import SearchFilter from "./SearchFilter";

type Props = {
    sessionIsActive: boolean,
    displayLogoutText: boolean;
}

const Homepage = (props: Props) => {
    const {sessionIsActive, displayLogoutText} = {...props};

    // const [isOperationActive, setIsOperationActive] = useState(false);
    const [bookId, setBookId] = useState(0);

    const handleClose = () => {
        const text = document.querySelector(".logout-text")!;
        text.remove();
    }

    const handleBorrow = () => {
        //not logged in
        if (!sessionIsActive) {
            console.log("not logged in")
        }

        //you already have this book borrowed
        //borrow
    }

    return ( 
    <main className="homepage">
        {displayLogoutText && 
            <Feedback text="You have successfully been logged out! Have a nice day" />
        }
        <BookList url='http://localhost:8000/books' operationType={OperationTypes.Borrow} bookId={bookId} setBookId={setBookId} handleOperation={handleBorrow} />
        <SearchFilter />
    </main> 
    );
}

// {displayLogoutText && 
//     <p className="logout-text" key={'logout-text'}>You have successfully been logged out! Have a nice day 
//         <button className="btn btn--smaller btn--greater-border-radius btn--margin" onClick={handleClose}>x</button>
//     </p>
// }
 
export default Homepage;