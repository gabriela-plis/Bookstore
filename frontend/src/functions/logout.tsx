
const logout = (setDisplayLogoutText: React.Dispatch<React.SetStateAction<boolean>>) => {
        sessionStorage.clear()
        setDisplayLogoutText(true);
}
 
export default logout;