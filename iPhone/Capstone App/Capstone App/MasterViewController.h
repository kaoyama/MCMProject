//
//  MasterViewController.h
//  Capstone App
//
//  Created by Blayne Kennedy on 11/17/12.
//  Copyright (c) 2012 Blayne Kennedy. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DetailViewController;

@interface MasterViewController : UITableViewController

@property (strong, nonatomic) DetailViewController *detailViewController;

@end
