import { useEffect, useState } from "react";
import BookList from "../../reusable-components/BookList";
import SearchFilter from "./SearchFilter";

type Props = {
    displayLogoutText: boolean;
}

const Homepage = (props: Props) => {

    const handleClose = () => {
        const text = document.querySelector(".logout-text")!;
        text.remove();
    }

    return ( 
    <main className="homepage">
        {props.displayLogoutText && <p className="logout-text" key={'logout-text'}>You have successfully been logged out! Have a nice day <button className="btn btn--close" onClick={() => handleClose()}>x</button></p>}
        <BookList url='http://localhost:8000/books' isBorrowBtn={true} isReturnBtn={false} />
        <SearchFilter />
    </main> 
    );
}
 
export default Homepage;