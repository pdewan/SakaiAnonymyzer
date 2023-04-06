package anonymyzer;

public class AssignmentMetrics {
	public int numFilesProcessed = 0;// files in these folders
	public int numLinesProcessed = 0; //total lines
	public int numCharactersProcessed = 0; //characters in total lines
	public int numFilesWithNames = 0; // files with users
	public int numLinesWithNames = 0; // matching using completely unstructured match
	public int numCharactersInLinesWithNames = 0; // characters in lines  matched unstructured
	public int numLinesWithPositives = 0; // num lines with positives

	public int numStructuredKeywordPositives = 0; //  matching using structured keywords match
	public int numUniqueStructuredPositives = 0; // token context around component match
	public int numCharactersInStructuredPositives = 0; // fragments in lines with unstructured but not structured matches

	public int numCharactersInUniqueStructuredPositives = 0; // fragments in positives to be examined with one word context

//	public int numUniqueNonContextualMatches = 0; // non context around component match
	public int numStructuredNegatives = 0; // a structured match that is not an unstructured match
	public int numCharactersInStructuredNegatives = 0; // a structured match that is not an unstructured match

	public int numUniqueStructuredNegatives = 0; // a unique structured match that is not an unstructured match
//	public int charactersInNonContextualPositives = 0; // fragments in positives to be examined with no context
	public int numCharactersInUniqueStructuredNegatives = 0; // fragments in lines with unstructured but not structured matches
	public int numWordsIgnored = 0; // englsh words that should not be replaced
	public int numUniqueWordsIgnored = 0; // for each word, count it once
	public int numWordsHidden = 0; // each word hidden
	public int numUniqueWordsHidden = 0; // num unique words
	
	public int numAliasesUsed = 0; 
	public int numUniqueAliasesUsed = 0;
	
	public int numMiddleNames = 0;
	public int numEmails = 0; // number of matches of the email pattern
	public int numNameClashes = 0;
	public int numFullNameResolutions = 0;
	public int numFullNameNoSpaceResolutions = 0;

	public int numNameComponentResolutions = 0;
	public int numNameReversalResolutions = 0;
//	public int numDropMiddleNameResolutions = 0;

	public int numNameDropResolutions = 0;
//	public int numLastNameResolutions = 0;
//	public int numFirstLastNameResolutions = 0;
//
//	public int numMiddleNameResolutions = 0;
//	public int numFirstMiddleNameResolutions = 0;
//	public int numMiddleLastNameResolutions = 0;
	


	


	
	public String toString() {
		return 
				"Number of middle names:" + numMiddleNames + "\n" +
				"Number of Files Processed:" + numFilesProcessed + "\n" +
				"Number of Lines Processed:" + numLinesProcessed + "\n" +
				"Number of Characters Processed:" + numCharactersProcessed + "\n" +
				"Number of Files With Names:" + numFilesWithNames + "\n" +
				"Number of Lines With Names:" + numLinesWithNames + "\n" +
				"Number of Characters in Lines With Names:" + numCharactersInLinesWithNames + "\n" +
				"Number of Positives:" + numStructuredKeywordPositives + "\n" +
				"Number of Unique Positives:" + numUniqueStructuredPositives + "\n" +
				"Number of Negatives:" + numStructuredNegatives + "\n" +
				"Number of Characters in Positives:" + numCharactersInStructuredPositives + "\n" +

				"Number of Characters in Unique Positives:" + numCharactersInUniqueStructuredPositives + "\n" +
				"Number of Characters in Negatives:" + numCharactersInStructuredNegatives + "\n" +

				"Number of Characters in Unique Negatives:" + numCharactersInUniqueStructuredNegatives + "\n" +
				"Number of Words Ignored:" + numWordsIgnored + "\n" +
				"Number of Unique Ignores:" + numUniqueWordsIgnored + "\n" +
				"Number of Words Hidden:" + numWordsHidden + "\n" +
				"Number of Emails:" + numEmails + "\n" + 
				"Number of Unique Hides:" + numUniqueWordsHidden + "\n" +
				"Number of Name Clashes:" + numNameClashes + "\n" +

				"Number of Aliases Used:" + numAliasesUsed + "\n" +
				"Number of Unique Aliases Used:" + numUniqueAliasesUsed + "\n" +
				"Number of Full Name Resolutions:" + numFullNameResolutions + "\n" +
				"Number of Full Name No Space Resolutions:" + numFullNameNoSpaceResolutions + "\n" +

				"Number of Name Component Resolutions:" + numNameComponentResolutions + "\n" +
//				"Number of Drop Middie Name Resolutions:" + numDropMiddleNameResolutions + "\n" +

				"Number of Name Permutation Resolutions:" + numNameDropResolutions + "\n" +
				"Number of Name Reversal Resolutions:" + numNameReversalResolutions + "\n" ;
//				"Number of First Name Resolutions:" + numFirstNameResolutions + "\n" +
//				"Number of Middle Name Resolutions:" + numMiddleNameResolutions + "\n" +
//				"Number of Last Name Resolutions:" + numLastNameResolutions + "\n" +

//				"Number of First Middle Name Resolutions:" + numFirstMiddleNameResolutions + "\n" +
//				"Number of Middle Last Name Resolutions:" + numMiddleLastNameResolutions + "\n" ;



	}

//	public int numChanges = 0;  

}
