import { useEffect, useState } from "react";
import BookList from "../../reusable-components/BookList";
import UserSidebar from "./Sidebar";
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import useFetch from "../../functions/useFetch";
import { User } from "../../DTO/User";
import Details from "./Details";
import Settings from "./Settings";
import AddBookPanel from "./AddBookPanel";
import RemoveBookPanel from "./RemoveBookPanel";
import Borrows from "./Borrows";

type Props = {
    setDisplayLogoutText: React.Dispatch<React.SetStateAction<boolean>>;
}

const UserPage = (props: Props) => {
    const user: User = useFetch('http://localhost:8000/users/' + sessionStorage.getItem("id")) as unknown as User;

    type userUrlPrefixIsPresent = {
        "borrows": boolean,
        "details": boolean,
        "settings": boolean,
        "add-book": boolean,
        "remove-book": boolean
    }

    const [urlPrefixesPresence, setUrlPrefixesPresence] = useState<userUrlPrefixIsPresent>({
        "borrows": true,
        "details": false,
        "settings": false,
        "add-book": false,
        "remove-book":false
    })

    const location = useLocation();

    useEffect( () => {
        const currentUrl = location.pathname;
        const prefixOfCurrentUrl = currentUrl.substring("/user/".length)
        
        for (const urlPrefix in urlPrefixesPresence) {

            setUrlPrefixesPresence(prevState => (
                {
                    ...prevState,
                    [urlPrefix]: false
                }
            )) 

            if (urlPrefix === prefixOfCurrentUrl) {
                setUrlPrefixesPresence(prevState => (
                    {
                        ...prevState,
                        [urlPrefix]: true
                    }
                )) 
            }
        }

    }, [location])

    return ( 
    <main className="UserPage">
        <UserSidebar employee={user.employee} setDisplayLogoutText={props.setDisplayLogoutText}/>
        {urlPrefixesPresence.borrows && <Borrows />}
        {urlPrefixesPresence.details && <Details />}
        {urlPrefixesPresence.settings && <Settings />}
        {urlPrefixesPresence["add-book"] && <AddBookPanel />}
        {urlPrefixesPresence["remove-book"] && <RemoveBookPanel />}
    </main> 
    );
}

export default UserPage;