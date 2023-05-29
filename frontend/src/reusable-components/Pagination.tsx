
type Props = {
    totalPages: number,
    paginate: (number: number) => void,
    currentPage: number
}

const Pagination = (props: Props) => {
    const {totalPages, paginate, currentPage} = {...props}

    const pageNumbers = [];
    
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(i);
    }

      return (
        <nav>
          <ul className='pagination'>
            {pageNumbers.map(number => (
              <li key={number} className='pagination__item'>     
                <button onClick={() => paginate(number)} className={currentPage === number ? 'pagination__link pagination__link--active' : 'pagination__link'}>
                  {number}
                </button>
              </li>
            ))}
          </ul>
        </nav>
      );

}

export default Pagination