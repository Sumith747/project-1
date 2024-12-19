// project structure
/src
  /main
    /java
      /com/example/webapp
        /controller
          - HomeController.java
          - UserController.java
          - ProductController.java
          - OrderController.java
        /service
          - UserService.java
          - ProductService.java
          - OrderService.java
        /model
          - User.java
          - Product.java
          - Order.java
          - Review.java
        /repository
          - UserRepository.java
          - ProductRepository.java
          - OrderRepository.java
        - WebAppApplication.java
    /resources
      /static
        /css
          - style.css
        /js
          - app.js
      /templates
        - home.html
        - about.html
        - contact.html
        - userProfile.html
        - productList.html
        - productDetails.html
        - login.html
        - orderSummary.html
    /application.properties

//web app application
package com.example.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class WebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }
}

@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/about", "/contact", "/login", "/register").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
            .logout()
                .permitAll();
    }
}


//user controller
package com.example.webapp.controller;

import com.example.webapp.model.User;
import com.example.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String authenticateUser(User user, Model model) {
        boolean isValidUser = userService.authenticateUser(user);
        if (isValidUser) {
            model.addAttribute("user", user);
            return "userProfile";
        } else {
            model.addAttribute("message", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        userService.saveUser(user);
        model.addAttribute("message", "User registered successfully!");
        return "login";
    }
}


//product controller
package com.example.webapp.controller;

import com.example.webapp.model.Product;
import com.example.webapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String viewProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "productList";
    }

    @GetMapping("/product/{id}")
    public String viewProductDetails(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "productDetails";
    }
}


//order controller
package com.example.webapp.controller;

import com.example.webapp.model.Order;
import com.example.webapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orderSummary")
    public String viewOrderSummary(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orderSummary";
    }
}


//product service
package com.example.webapp.service;

import com.example.webapp.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public Product[] getAllProducts() {
        return new Product[] {
            new Product(1, "Car Model A", 1000),
            new Product(2, "Car Model B", 1200),
            new Product(3, "Car Model C", 1500)
        };
    }

    public Product getProductById(int id) {
        // Simulating fetching product by id from a database
        return new Product(id, "Car Model " + id, 1000 + (id * 100));
    }
}


//order service
package com.example.webapp.service;

import com.example.webapp.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public Order[] getAllOrders() {
        return new Order[] {
            new Order(1, "Order A", 1000),
            new Order(2, "Order B", 1200),
            new Order(3, "Order C", 1500)
        };
    }
}


//user model
package com.example.webapp.model;

public class User {
    private String username;
    private String password;
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


//product model
package com.example.webapp.model;

public class Product {
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


//order summary
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Summary</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <header>
        <h1>Your Orders</h1>
    </header>
    <main>
        <h2>Order Details</h2>
        <table>
            <tr>
                <th>Order ID</th>
                <th>Name</th>
                <th>Price</th>
            </tr>
            <tr th:each="order : ${orders}">
                <td th:text="${order.id}"></td>
                <td th:text="${order.name}"></td>
                <td th:text="${order.price}"></td>
            </tr>
        </table>
    </main>
    <footer>
        <p>&copy; 2024 Car Racing WebApp</p>
    </footer>
</body>
</html>


//style css
body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f4f4f4;
}

header {
    background-color: #333;
    color: white;
    padding: 20px;
    text-align: center;
}

main {
    padding: 20px;
    text-align: center;
}

table {
    width: 80%;
    margin: 20px auto;
    border-collapse: collapse;
}

table th, table td {
    border: 1px solid #ddd;
    padding: 10px;
}

footer {
    background-color: #333;
    color: white;
    padding: 10px;
    text-align: center;
}





// leet code 
// two sum
import java.util.HashMap;
public class Solution {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> numMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (numMap.containsKey(complement)) {
                return new int[] { numMap.get(complement), i };
            }
            numMap.put(nums[i], i);
        }
        return new int[] {};
    }
}


//Add Two Numbers
public class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            carry = sum / 10;
            current.next = new ListNode(sum % 10);
            current = current.next; 
        }

        return dummy.next;
    }
}

//Longest Substring Without Repeating Characters
import java.util.HashSet;

public class Solution {
    
    public static int lengthOfLongestSubstring(String s) {
        // HashSet to store unique characters in the current window
        HashSet<Character> charSet = new HashSet<>();
        int left = 0;  // Left pointer of the sliding window
        int maxLen = 0;  // Maximum length of the substring without repeating characters
        
        // Iterate through the string with the right pointer
        for (int right = 0; right < s.length(); right++) {
            // If the character is already in the set, move the left pointer
            while (charSet.contains(s.charAt(right))) {
                charSet.remove(s.charAt(left));
                left++;
            }
            
            // Add the current character to the set
            charSet.add(s.charAt(right));

            // Update the maximum length of the substring
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }

    public static void main(String[] args) {
        // Example 1: "abcabcbb"
        String s1 = "abcabcbb";
        System.out.println("Longest substring length: " + lengthOfLongestSubstring(s1));  // Output: 3

        // Example 2: "bbbbb"
        String s2 = "bbbbb";
        System.out.println("Longest substring length: " + lengthOfLongestSubstring(s2));  // Output: 1

        // Example 3: "pwwkew"
        String s3 = "pwwkew";
        System.out.println("Longest substring length: " + lengthOfLongestSubstring(s3));  // Output: 3
    }
}

//Median of Two Sorted Arrays
public class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // Ensure nums1 is the smaller array for binary search efficiency
        if (nums1.length > nums2.length) {
            int[] temp = nums1;
            nums1 = nums2;
            nums2 = temp;
        }

        int m = nums1.length;
        int n = nums2.length;
        
        int left = 0, right = m;

        while (left <= right) {
            // Partition nums1
            int partition1 = (left + right) / 2;
            // Partition nums2, derived from partition1
            int partition2 = (m + n + 1) / 2 - partition1;

            // Find the elements around the partition in nums1 and nums2
            int maxLeft1 = (partition1 == 0) ? Integer.MIN_VALUE : nums1[partition1 - 1];
            int minRight1 = (partition1 == m) ? Integer.MAX_VALUE : nums1[partition1];

            int maxLeft2 = (partition2 == 0) ? Integer.MIN_VALUE : nums2[partition2 - 1];
            int minRight2 = (partition2 == n) ? Integer.MAX_VALUE : nums2[partition2];

            // Check if we have found the correct partition
            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                // If the combined length is odd, return the maximum of the left partition
                if ((m + n) % 2 == 1) {
                    return Math.max(maxLeft1, maxLeft2);
                } else {
                    // If the combined length is even, return the average of the middle two elements
                    return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2.0;
                }
            } else if (maxLeft1 > minRight2) {
                // Move partition1 to the left
                right = partition1 - 1;
            } else {
                // Move partition1 to the right
                left = partition1 + 1;
            }
        }

        throw new IllegalArgumentException("Input arrays are not sorted");
    }
}


//Longest Palindromic Substring
public class Solution {
    public String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }

        String longest = "";
        
        for (int i = 0; i < s.length(); i++) {
            // Check for palindrome with a single character center
            String oddPalindrome = expandAroundCenter(s, i, i);
            // Check for palindrome with two character center
            String evenPalindrome = expandAroundCenter(s, i, i + 1);
            
            // Update the longest palindrome if necessary
            if (oddPalindrome.length() > longest.length()) {
                longest = oddPalindrome;
            }
            if (evenPalindrome.length() > longest.length()) {
                longest = evenPalindrome;
            }
        }
        
        return longest;
    }
    
    // Helper function to expand around the center
    private String expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        // Return the palindrome substring
        return s.substring(left + 1, right);
    }
}


//Zigzag Conversion
public class Solution {
    public String convert(String s, int numRows) {
        // Edge case: if the number of rows is 1, no zigzag is possible
        if (numRows == 1) {
            return s;
        }
        
        // Initialize an array of StringBuilder to store each row
        StringBuilder[] rows = new StringBuilder[numRows];
        for (int i = 0; i < numRows; i++) {
            rows[i] = new StringBuilder();
        }
        
        int currentRow = 0;
        boolean goingDown = false;
        
        // Iterate through the string and fill the rows
        for (char c : s.toCharArray()) {
            rows[currentRow].append(c);
            
            // If we're at the top or bottom row, change the direction
            if (currentRow == 0 || currentRow == numRows - 1) {
                goingDown = !goingDown;
            }
            
            // Move to the next row
            currentRow += goingDown ? 1 : -1;
        }
        
        // Combine all rows into one string
        StringBuilder result = new StringBuilder();
        for (StringBuilder row : rows) {
            result.append(row);
        }
        
        return result.toString();
    }
}


//Reverse Integer
public class Solution {
    public int reverse(int x) {
        int result = 0;
        int max = Integer.MAX_VALUE / 10;
        int min = Integer.MIN_VALUE / 10;
        
        while (x != 0) {
            int digit = x % 10; // Get the last digit
            x /= 10; // Remove the last digit from x
            
            // Check for overflow before updating result
            if (result > max || (result == max && digit > 7)) {
                return 0; // Overflow for positive numbers
            }
            if (result < min || (result == min && digit < -8)) {
                return 0; // Overflow for negative numbers
            }
            
            // Update result with the new digit
            result = result * 10 + digit;
        }
        
        return result;
    }
}


// String to Integer (atoi)
public class Solution {
    public int myAtoi(String s) {
        int i = 0;
        int n = s.length();
        
        // Step 1: Skip leading whitespaces
        while (i < n && s.charAt(i) == ' ') {
            i++;
        }

        // Step 2: Check for the sign
        int sign = 1;
        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
            sign = (s.charAt(i) == '-') ? -1 : 1;
            i++;
        }

        // Step 3: Read the digits and convert them to an integer
        long result = 0;  // Use long to prevent overflow during calculation
        while (i < n && Character.isDigit(s.charAt(i))) {
            result = result * 10 + (s.charAt(i) - '0');
            i++;
            
            // Step 4: Check for overflow
            if (result > Integer.MAX_VALUE) {
                return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
        }

        // Step 5: Apply the sign and return the result
        return (int) (result * sign);
    }
}


//Palindrome Number
public class Solution {
    public boolean isPalindrome(int x) {
        // If the number is negative or if the number ends with 0 but is not 0 itself, return false
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }
        
        int reversed = 0;
        while (x > reversed) {
            // Build the reversed half of the number
            reversed = reversed * 10 + x % 10;
            x /= 10;
        }
        
        // If the original number is equal to the reversed half or if it is the same when divided by 10 (for odd length numbers)
        return x == reversed || x == reversed / 10;
    }
}



//Regular Expression Matching
public class Solution {
    public boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();
        
        // dp[i][j] represents whether s[0..i-1] matches p[0..j-1]
        boolean[][] dp = new boolean[m + 1][n + 1];
        
        // Initial condition: empty string matches empty pattern
        dp[0][0] = true;
        
        // Handle the case when the pattern starts with a '*' (matching empty string)
        for (int j = 1; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2];  // '*' means zero occurrences of the previous character
            }
        }
        
        // Fill the dp table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (p.charAt(j - 1) == s.charAt(i - 1) || p.charAt(j - 1) == '.') {
                    // If characters match or pattern has '.', carry forward the previous result
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (p.charAt(j - 1) == '*') {
                    // Case 1: '*' represents zero occurrences of the preceding character
                    dp[i][j] = dp[i][j - 2];
                    
                    // Case 2: '*' represents one or more occurrences of the preceding character
                    if (p.charAt(j - 2) == s.charAt(i - 1) || p.charAt(j - 2) == '.') {
                        dp[i][j] |= dp[i - 1][j];
                    }
                }
            }
        }
        
        // The result is stored in dp[m][n], where m = s.length() and n = p.length()
        return dp[m][n];
    }
}


//Container With Most Water
public class Solution {
    public int maxArea(int[] height) {
        int left = 0;        // Pointer at the beginning of the array
        int right = height.length - 1;  // Pointer at the end of the array
        int maxArea = 0;     // Variable to store the maximum area found
        
        while (left < right) {
            // Calculate the area with the current left and right pointers
            int width = right - left;
            int currentHeight = Math.min(height[left], height[right]);
            int currentArea = width * currentHeight;
            
            // Update maxArea if the current area is larger
            maxArea = Math.max(maxArea, currentArea);
            
            // Move the pointer that points to the shorter line
            if (height[left] < height[right]) {
                left++;  // Move left pointer to the right
            } else {
                right--; // Move right pointer to the left
            }
        }
        
        return maxArea;
    }
}


//Integer to Roman
public class Solution {
    public String intToRoman(int num) {
        // Define the Roman numeral symbols and their corresponding values.
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        
        StringBuilder roman = new StringBuilder();
        
        // Iterate through all values
        for (int i = 0; i < values.length; i++) {
            // While the number is greater than or equal to the current value
            while (num >= values[i]) {
                // Append the symbol for the current value to the result
                roman.append(symbols[i]);
                // Subtract the current value from num
                num -= values[i];
            }
        }
        
        return roman.toString();
    }
}


//Roman to Integer
public class Solution {
    public int romanToInt(String s) {
        // Define a map for Roman numeral values
        Map<Character, Integer> romanToValue = new HashMap<>();
        romanToValue.put('I', 1);
        romanToValue.put('V', 5);
        romanToValue.put('X', 10);
        romanToValue.put('L', 50);
        romanToValue.put('C', 100);
        romanToValue.put('D', 500);
        romanToValue.put('M', 1000);

        int total = 0;
        
        // Iterate through the string, comparing each character to the next one
        for (int i = 0; i < s.length(); i++) {
            // If the current value is less than the next value, subtract it
            if (i + 1 < s.length() && romanToValue.get(s.charAt(i)) < romanToValue.get(s.charAt(i + 1))) {
                total -= romanToValue.get(s.charAt(i));
            } else {
                total += romanToValue.get(s.charAt(i));
            }
        }
        
        return total;
    }
}


//Longest Common Prefix
public class Solution {
    public String longestCommonPrefix(String[] strs) {
        // Check if the input array is empty
        if (strs == null || strs.length == 0) {
            return "";
        }
        
        // Start with the first string as the common prefix
        String prefix = strs[0];
        
        // Compare the prefix with each string in the array
        for (int i = 1; i < strs.length; i++) {
            // While the current string does not start with the prefix
            while (strs[i].indexOf(prefix) != 0) {
                // Shorten the prefix by one character
                prefix = prefix.substring(0, prefix.length() - 1);
                // If no common prefix exists, return an empty string
                if (prefix.isEmpty()) {
                    return "";
                }
            }
        }
        
        // Return the longest common prefix
        return prefix;
    }
}


//3Sum
import java.util.*;

public class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        
        // Sort the array to facilitate the two-pointer approach
        Arrays.sort(nums);
        
        // Loop through the array with the first pointer
        for (int i = 0; i < nums.length - 2; i++) {
            // Skip duplicate values for the first pointer to avoid duplicate triplets
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // Two-pointer approach to find pairs that sum up to -nums[i]
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                if (sum == 0) {
                    // Found a valid triplet
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    // Skip duplicates for the second pointer (left)
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    
                    // Skip duplicates for the third pointer (right)
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    
                    // Move both pointers inward
                    left++;
                    right--;
                } else if (sum < 0) {
                    // If the sum is too small, move the left pointer to the right to increase the sum
                    left++;
                } else {
                    // If the sum is too large, move the right pointer to the left to decrease the sum
                    right--;
                }
            }
        }
        
        return result;
    }
}


//3Sum Closest
import java.util.*;

public class Solution {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);  // Step 1: Sort the array
        int closestSum = Integer.MAX_VALUE;  // Initialize with a very large value
        
        for (int i = 0; i < nums.length - 2; i++) {  // Step 2: Iterate through the array with the first pointer
            if (i > 0 && nums[i] == nums[i - 1]) {  // Skip duplicate elements to avoid repeating triplets
                continue;
            }
            
            int left = i + 1, right = nums.length - 1;  // Step 3: Initialize two pointers for the second and third elements
            
            while (left < right) {  // Step 4: Use the two-pointer approach to find a triplet
                int sum = nums[i] + nums[left] + nums[right];  // Calculate the sum
                
                if (sum == target) {
                    return sum;  // Step 5: If the sum equals the target, return it immediately
                }
                
                // Step 6: Update the closest sum if the current sum is closer to the target
                if (Math.abs(sum - target) < Math.abs(closestSum - target)) {
                    closestSum = sum;
                }
                
                // Step 7: Adjust the pointers based on the sum
                if (sum < target) {
                    left++;  // If the sum is too small, move the left pointer to the right
                } else {
                    right--;  // If the sum is too large, move the right pointer to the left
                }
            }
        }
        
        return closestSum;  // Step 8: Return the closest sum found
    }
}


//Letter Combinations of a Phone Number
import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<String> letterCombinations(String digits) {
        // If the input string is empty, return an empty list
        if (digits == null || digits.length() == 0) {
            return new ArrayList<>();
        }

        // Mapping of digits to corresponding letters on the phone keypad
        String[] phoneMap = {
            "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
        };

        // List to store the result combinations
        List<String> result = new ArrayList<>();
        
        // Helper method to perform backtracking
        backtrack(digits, 0, "", result, phoneMap);
        
        return result;
    }

    private void backtrack(String digits, int index, String currentCombination, 
                           List<String> result, String[] phoneMap) {
        // If the current combination is of the same length as digits, add it to the result
        if (index == digits.length()) {
            result.add(currentCombination);
            return;
        }

        // Get the letters corresponding to the current digit
        char digit = digits.charAt(index);
        String letters = phoneMap[digit - '0'];
        
        // Loop through the letters for the current digit
        for (int i = 0; i < letters.length(); i++) {
            // Recur to the next digit
            backtrack(digits, index + 1, currentCombination + letters.charAt(i), result, phoneMap);
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        // Example usage
        System.out.println(solution.letterCombinations("23")); // Expected: [ad, ae, af, bd, be, bf, cd, ce, cf]
    }
}


// 4Sum
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DriverSolution {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[] nums1 = {1, 0, -1, 0, -2, 2};
        int target1 = 0;
        System.out.println(solution.fourSum(nums1, target1));

        int[] nums2 = {2, 2, 2, 2, 2};
        int target2 = 8;
        System.out.println(solution.fourSum(nums2, target2));
    }
}

class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 4) {
            return result;
        }

        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 3; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            for (int j = i + 1; j < nums.length - 2; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }

                int left = j + 1;
                int right = nums.length - 1;

                while (left < right) {
                    long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];
                    if (sum == target) {
                        result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));

                        while (left < right && nums[left] == nums[left + 1]) {
                            left++;
                        }

                        while (left < right && nums[right] == nums[right - 1]) {
                            right--;
                        }

                        left++;
                        right--;
                    } else if (sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }

        return result;
    }
}


// Remove Nth Node From End of List
class ListNode {
    int val;
    ListNode next;
    
    // Constructor
    ListNode() {}
    
    ListNode(int val) {
        this.val = val;
    }
    
    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
    
    // Deserialize the string into a ListNode
    public static ListNode deserialize(String s) {
        // Remove any spaces, and also strip the brackets
        s = s.replaceAll("\\s", "").replaceAll("[\\[\\]]", "");
        
        // If the string is empty, return null
        if (s.isEmpty()) {
            return null;
        }
        
        // Split the string by commas
        String[] nodes = s.split(",");
        
        // Create the head node from the first element
        ListNode head = new ListNode(Integer.parseInt(nodes[0]));
        ListNode current = head;
        
        // Create the rest of the nodes
        for (int i = 1; i < nodes.length; i++) {
            current.next = new ListNode(Integer.parseInt(nodes[i]));
            current = current.next;
        }
        
        return head;
    }
    
    // Helper method to convert ListNode to String
    public static String serialize(ListNode head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.val).append(",");
            head = head.next;
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }
}

public class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // Create a dummy node that points to the head of the list
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // Initialize two pointers, both starting at the dummy node
        ListNode first = dummy;
        ListNode second = dummy;
        
        // Move the first pointer n + 1 steps ahead, so the gap between first and second is n nodes
        for (int i = 0; i <= n; i++) {
            first = first.next;
        }
        
        // Move both pointers one step at a time until the first pointer reaches the end
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        
        // Remove the nth node from the end
        second.next = second.next.next;
        
        // Return the head of the modified list (dummy.next)
        return dummy.next;
    }
    
    public static void main(String[] args) {
        // Deserialize input string into ListNode
        ListNode head = ListNode.deserialize("[1,2,3,4,5]");
        int n = 2;
        
        // Remove the nth node from the end of the list
        Solution solution = new Solution();
        ListNode result = solution.removeNthFromEnd(head, n);
        
        // Serialize the result and print it
        String resultStr = ListNode.serialize(result);
        System.out.println(resultStr);  // Output: 1,2,3,5
    }
}


//Valid Parentheses
import java.util.Stack;

public class Solution {
    public boolean isValid(String s) {
        // Stack to store opening brackets
        Stack<Character> stack = new Stack<>();
        
        // Iterate through each character in the string
        for (char c : s.toCharArray()) {
            // If the character is an opening bracket, push it onto the stack
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } 
            // If the character is a closing bracket, check for corresponding opening bracket
            else {
                // If stack is empty or the top of the stack doesn't match, return false
                if (stack.isEmpty()) {
                    return false;
                }
                char top = stack.pop();
                if (c == ')' && top != '(' || c == '}' && top != '{' || c == ']' && top != '[') {
                    return false;
                }
            }
        }
        
        // If the stack is empty, all opening brackets have been properly matched
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // Example test cases
        System.out.println(solution.isValid("()")); // true
        System.out.println(solution.isValid("()[]{}")); // true
        System.out.println(solution.isValid("(]")); // false
        System.out.println(solution.isValid("([])")); // true
    }
}


//Merge Two Sorted Lists
class ListNode {
    int val;
    ListNode next;
    
    // Constructors
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    
    // Helper method to print the list for debugging purposes
    public static void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();
    }
    
    // Deserialize method to create a linked list from a string representation
    public static ListNode deserialize(String data) {
        if (data == null || data.length() == 0 || data.equals("[]")) {
            return null; // Empty list
        }
        
        // Remove the brackets and split the numbers
        data = data.substring(1, data.length() - 1);  // Remove '[' and ']'
        String[] nodes = data.split(",");
        
        ListNode head = new ListNode(Integer.parseInt(nodes[0]));
        ListNode current = head;
        
        for (int i = 1; i < nodes.length; i++) {
            current.next = new ListNode(Integer.parseInt(nodes[i]));
            current = current.next;
        }
        
        return head;
    }
}

public class Solution {
    
    // Method to merge two sorted linked lists
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                current.next = list1;
                list1 = list1.next;
            } else {
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }
        
        // If any list is left, append it
        if (list1 != null) {
            current.next = list1;
        } else {
            current.next = list2;
        }
        
        return dummy.next;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // Example 1: Input: [1, 2, 4], [1, 3, 4]
        ListNode list1 = ListNode.deserialize("[1,2,4]");
        ListNode list2 = ListNode.deserialize("[1,3,4]");
        ListNode result1 = solution.mergeTwoLists(list1, list2);
        ListNode.printList(result1);  // Expected output: 1 1 2 3 4 4

        // Example 2: Input: [], []
        ListNode list3 = ListNode.deserialize("[]");
        ListNode list4 = ListNode.deserialize("[]");
        ListNode result2 = solution.mergeTwoLists(list3, list4);
        ListNode.printList(result2);  // Expected output: (empty)

        // Example 3: Input: [], [0]
        ListNode list5 = ListNode.deserialize("[]");
        ListNode list6 = ListNode.deserialize("[0]");
        ListNode result3 = solution.mergeTwoLists(list5, list6);
        ListNode.printList(result3);  // Expected output: 0
    }
}


// Generate Parentheses
import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, "", 0, 0, n);
        return result;
    }
    
    private void backtrack(List<String> result, String current, int open, int close, int n) {
        // Base case: if the current string has 2 * n characters, it's a valid combination
        if (current.length() == 2 * n) {
            result.add(current);
            return;
        }
        
        // Add an opening parenthesis if we haven't used n opening parentheses yet
        if (open < n) {
            backtrack(result, current + "(", open + 1, close, n);
        }
        
        // Add a closing parenthesis if we have more open parentheses than close parentheses
        if (close < open) {
            backtrack(result, current + ")", open, close + 1, n);
        }
    }
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // Test Case 1
        List<String> result1 = solution.generateParenthesis(3);
        System.out.println(result1);  // Output: ["((()))","(()())","(())()","()(())","()()()"]
        
        // Test Case 2
        List<String> result2 = solution.generateParenthesis(1);
        System.out.println(result2);  // Output: ["()"]
    }
}


//Merge k Sorted Lists
import java.util.PriorityQueue;
import com.eclipsesource.json.JsonArray;

class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
        this.next = null;
    }

    static ListNode arrayToListNode(JsonArray jsonArray) {
        if (jsonArray == null || jsonArray.size() == 0) {
            return null;
        }

        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        for (int i = 0; i < jsonArray.size(); i++) {
            current.next = new ListNode(jsonArray.get(i).asInt());
            current = current.next;
        }

        return dummy.next;
    }
}

class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }

        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);

        for (ListNode node : lists) {
            if (node != null) {
                minHeap.offer(node);
            }
        }

        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while (!minHeap.isEmpty()) {
            ListNode smallestNode = minHeap.poll();
            current.next = smallestNode;
            current = current.next;

            if (smallestNode.next != null) {
                minHeap.offer(smallestNode.next);
            }
        }

        return dummy.next;
    }
}

public class Driver {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // Example usage:
        ListNode[] lists = new ListNode[3];
        lists[0] = new ListNode(1);
        lists[0].next = new ListNode(4);
        lists[0].next.next = new ListNode(5);

        lists[1] = new ListNode(1);
        lists[1].next = new ListNode(3);
        lists[1].next.next = new ListNode(4);

        lists[2] = new ListNode(2);
        lists[2].next = new ListNode(6);

        ListNode result = solution.mergeKLists(lists);

        // Print the result
        while (result != null) {
            System.out.print(result.val + " ");
            result = result.next;
        }
    }
}


//Swap Nodes in Pairs
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
        this.next = null;
    }

    // Method to deserialize a string to a ListNode
    static ListNode deserialize(String data) {
        if (data == null || data.isEmpty() || data.equals("[]")) {
            return null;
        }

        // Remove the square brackets
        data = data.substring(1, data.length() - 1);

        String[] values = data.split(",");
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        for (String value : values) {
            current.next = new ListNode(Integer.parseInt(value.trim()));
            current = current.next;
        }

        return dummy.next;
    }

    // Method to print the list (for demonstration purposes)
    static void printList(ListNode head) {
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val + " ");
            current = current.next;
        }
        System.out.println();
    }
}

class Solution {
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;

        while (prev.next != null && prev.next.next != null) {
            ListNode first = prev.next;
            ListNode second = prev.next.next;

            first.next = second.next;
            second.next = first;
            prev.next = second;

            prev = first;
        }

        return dummy.next;
    }
}

public class DriverSolution {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // Example 1
        ListNode head1 = ListNode.deserialize("[1,2,3,4]");
        ListNode result1 = solution.swapPairs(head1);
        ListNode.printList(result1);

        // Example 2
        ListNode head2 = ListNode.deserialize("[]");
        ListNode result2 = solution.swapPairs(head2);
        ListNode.printList(result2);

        // Example 3
        ListNode head3 = ListNode.deserialize("[1]");
        ListNode result3 = solution.swapPairs(head3);
        ListNode.printList(result3);

        // Example 4
        ListNode head4 = ListNode.deserialize("[1,2,3]");
        ListNode result4 = solution.swapPairs(head4);
        ListNode.printList(result4);
    }
}
